package com.ll.autotransaction.job;

import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.StockConfigService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.config.BrokerageConfig;
import com.ll.autotransaction.service.model.ApplyDataInfo;
import com.ll.autotransaction.service.model.TransactionParam;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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


//    @Scheduled(cron= "*/2 * * * * ?") //每2秒运行一次
    public void doJob(){
        this.updateConfig();
        if(BrokerageConfig.enableAutoTransaction&&isTransactionTime()){
            if(isWindUp()){
                if(LocalDate.now().isAfter(BrokerageConfig.windUpDate)){
                    //收盘撤单
                    brokerageService.revokeOrders();
                    BrokerageConfig.applyDataInfos = new ArrayList<>();
                    BrokerageConfig.windUpDate = LocalDate.now();
                }
            }else {
                //正常交易时间段
                //判断是否需要挂单
                if(LocalDate.now().isAfter(BrokerageConfig.pendingOrderDate)){
                    //先撤单
                    brokerageService.revokeOrders();
                    BrokerageConfig.applyDataInfos = new ArrayList<>();
                    var enableList = stockConfigService.listEnable();
                    //挂单
                    if(enableList.size()>0){
                        this.pendingOrders(enableList);
                    }
                    BrokerageConfig.pendingOrderDate = LocalDate.now();
                }
                var todayDealList = brokerageService.getTodayHisDealData();
                if(todayDealList.size()>0){
                    var deleteList = new ArrayList<ApplyDataInfo>();
                    for (var dealItem : todayDealList){
                        for (var applyItem : BrokerageConfig.applyDataInfos){
                            //有新的成交
                            if(dealItem.getApplyCode().equals(applyItem.getApplyCode())){
                                if(dealItem.getApplyType().equals("证券买入")){
                                    this.removeApplyItem(dealItem.getCode(),"证券卖出");

                                }else {
                                    this.removeApplyItem(dealItem.getCode(),"证券买入");

                                }
                                deleteList.add(applyItem);
                            }
                        }
                    }
                    BrokerageConfig.applyDataInfos.removeAll(deleteList);
                }
            }

        }
    }


    private void removeApplyItem(String code,String type){
        var applyList = BrokerageConfig.applyDataInfos.stream().filter(a->a.getCode().equals(code)&&a.getApplyType().equals(type)).collect(Collectors.toList());
        if(applyList.size()>0){
            for (var item:applyList){
                brokerageService.revokeOrders(item.getApplyCode());
            }
        }
    }


    private void pendingOrders(List<StockConfigDo> enableList){
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


    private void updateConfig(){
        var userInfo = userService.getUserByAccount("13226546881");
        BrokerageConfig.dfcfCookies = userInfo.getCookies();
        BrokerageConfig.dfcfValidateCode = userInfo.getValidateCode();
        BrokerageConfig.dfcfHost = userInfo.getHost();
        BrokerageConfig.enableAutoTransaction = userInfo.getEnableAutoTransaction()==1;

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
        return result;
    }




}
