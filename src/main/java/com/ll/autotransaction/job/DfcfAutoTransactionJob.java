package com.ll.autotransaction.job;

import com.ll.autotransaction.dao.mode.DealLogDo;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.DealLogService;
import com.ll.autotransaction.service.StockConfigService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.config.BrokerageConfig;
import com.ll.autotransaction.service.model.ApplyDataInfo;
import com.ll.autotransaction.service.model.TransactionParam;
import com.ll.autotransaction.util.EmailUtil;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DfcfAutoTransactionJob {

    List<ApplyDataInfo> applyDataInfoList = new ArrayList<>();

    @Autowired
    UserService userService;

    @Autowired
    BrokerageService brokerageService;

    @Autowired
    StockConfigService stockConfigService;

    @Autowired
    DealLogService dealLogService;


    @Autowired
    EmailUtil emailUtil;

    @Scheduled(cron= "*/4 * * * * ?") //每4秒运行一次
    public void autoTransaction() throws Exception {
        try{
            this.doJob();
        }catch (Exception e){
            //一旦报错即刻停止
            BrokerageConfig.enableAutoTransaction=false;
            userService.setEnableAutoTransaction(false);
            emailUtil.sent("出错了",e.toString());
        }
    }



    public void doJob() throws Exception {
        userService.updateConfig();
        if(BrokerageConfig.enableAutoTransaction&&isTransactionTime()){
            if(isWindUp()){
                if(LocalDate.now().isAfter(BrokerageConfig.windUpDate)){
                    //收盘撤单
                    brokerageService.revokeOrders();
                    BrokerageConfig.applyDataInfos = new ArrayList<>();
                    BrokerageConfig.windUpDate = LocalDate.now();
                    emailUtil.sent("撤单提示","收盘撤单成功");
                }
            }else {
                //正常交易时间段
                //判断是否需要挂单
                if(LocalDate.now().isAfter(BrokerageConfig.pendingOrderDate)){
                    var enableList = stockConfigService.listEnable();
                    var enableCodeList = enableList.stream().map(e->e.getCode()).collect(Collectors.toList());
                    //先撤单
                    brokerageService.revokeOrdersByCode(enableCodeList);
                    BrokerageConfig.applyDataInfos = new ArrayList<>();
                    //后续需要添加判断是否大跌大涨的挂单。
                    //挂单
                    if(enableList.size()>0){
                        this.pendingOrders(enableList);
                    }
                    emailUtil.sent("已根据配置挂单",enableList.toString());
                    BrokerageConfig.pendingOrderDate = LocalDate.now();
                }
                var todayDealList = brokerageService.getTodayHisDealData();
                if(todayDealList.size()>0){
                    var newDealList = new ArrayList<ApplyDataInfo>();
                    for (var dealItem : todayDealList){
                        for (var applyItem : BrokerageConfig.applyDataInfos){
                            //判断是否有新的成交
                            if(dealItem.getApplyCode().equals(applyItem.getApplyCode())){
                                newDealList.add(applyItem);
                            }
                        }
                    }
                    //处理成交结果
                    if(newDealList.size()>0){
                        this.newDealEvent(newDealList);
                    }
                }
            }

        }
    }


    private void newDealEvent(List<ApplyDataInfo> newDealList) throws Exception {
        var stockConfigList = new ArrayList<StockConfigDo>();
        for (var dealItem:newDealList){
            //先撤单
            if(dealItem.getApplyType().equals("证券买入")){
                this.removeApplyItem(dealItem.getCode(),"证券卖出");
            }else {
                this.removeApplyItem(dealItem.getCode(),"证券买入");
            }
            //移除本地缓存记录
            BrokerageConfig.removeApplyDataInfoForCode(dealItem.getCode());
            //重置价格点位
            stockConfigService.editPrice(dealItem.getCode(),dealItem.getPrice());
            //记录成交日志
            var dealLog = new DealLogDo(){{
                setCode(dealItem.getCode());
                setName(dealItem.getName());
                setPrice(dealItem.getPrice());
                setCount(dealItem.getCount());
                setType(dealItem.getApplyType().equals("证券买入")?0:1);
                setCreateTime(LocalDateTime.now());
            }};
            dealLogService.add(dealLog);
            //重新挂单
            var stockConfig = stockConfigService.getItemByCode(dealItem.getCode());
            stockConfigList.add(stockConfig);
            //发送短信提示
            emailUtil.sent(String.format("%s：%s",dealItem.getName(),dealItem.getApplyType()),String.format("价格：%s",dealItem.getPrice()));
        }
        this.pendingOrders(stockConfigList);

    }





    private void removeApplyItem(String code,String type){
        var applyList = BrokerageConfig.applyDataInfos.stream().filter(a->a.getCode().equals(code)&&a.getApplyType().equals(type)).collect(Collectors.toList());
        if(applyList.size()>0){
            for (var item:applyList){
                brokerageService.revokeOrders(item.getApplyCode());
            }
        }
    }


    private void pendingOrders(List<StockConfigDo> enableList) throws Exception {
        var applyCodeList = new ArrayList<String>();
        for (var enableItem : enableList){
            var param = new TransactionParam(){{
                setCode(enableItem.getCode());
                setName(enableItem.getName());
                setCount(enableItem.getCount());
                setPrice(enableItem.getLowPrice());
            }};
            var buyCode = brokerageService.buy(param);
            param.setPrice(enableItem.getHighPrice());
            var sellCode =  brokerageService.sell(param);
            applyCodeList.add(buyCode);
            applyCodeList.add(sellCode);
        }
        var todayOrders = brokerageService.getTodayOrdersData(null);
        for (var todayOrder:todayOrders){
            for (var applyCode:applyCodeList) {
             if(todayOrder.getApplyCode().equals(applyCode)){
                 BrokerageConfig.applyDataInfos.add(todayOrder);
             }
            }
        }
    }






    /**
     * 是否收盘时间
     * @return
     */
    private boolean isWindUp(){
        boolean result = false;
        if(LocalTime.now().isAfter(LocalTime.of(14,49,0))&&LocalTime.now().isBefore(LocalTime.of(15,00,0))){
            result = true;
        }
        return result;
    }


    /**
     * 是否是交易时间
     * @return
     */
    private boolean isTransactionTime(){
        boolean result = false;
        if(LocalDate.now().getDayOfWeek().getValue()>=1&&LocalDate.now().getDayOfWeek().getValue()<=5){
            if((LocalTime.now().isAfter(LocalTime.of(9,20,00))&&LocalTime.now().isBefore(LocalTime.of(11,30,0)))||
                    (LocalTime.now().isAfter(LocalTime.of(13,00,00))&&LocalTime.now().isBefore(LocalTime.of(15,00,0)))){
                result = true;
            }
        }
        return true;
    }




}
