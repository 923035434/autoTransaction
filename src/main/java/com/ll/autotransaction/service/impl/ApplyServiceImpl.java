package com.ll.autotransaction.service.impl;

import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.*;
import com.ll.autotransaction.service.config.BrokerageConfig;
import com.ll.autotransaction.service.model.StockApplyConfig;
import com.ll.autotransaction.service.model.TransactionParam;
import com.ll.autotransaction.util.EmailUtil;
import com.ll.autotransaction.util.PriceUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    BrokerageService brokerageService;

    @Autowired
    StockConfigService stockConfigService;

    @Autowired
    DealLogService dealLogService;

    @Autowired
    PriceService priceService;


    @Autowired
    EmailUtil emailUtil;


    @Override
    public boolean pendingOrders() throws Exception {
        var enableList = stockConfigService.listEnable();
        var enableCodeList = enableList.stream().map(e->e.getCode()).collect(Collectors.toList());
        //先撤单
        brokerageService.revokeOrdersByCode(enableCodeList);
        BrokerageConfig.applyDataInfos = new ArrayList<>();
        //挂单
        if(enableList.size()>0){
            //判断开盘是否有大涨大跌
            var calEnableList = this.CalPrice(enableList);
            this.pendingEnableOrders(calEnableList);
        }
        emailUtil.sent("已根据配置挂单",enableList.toString());
        BrokerageConfig.pendingOrderDate = LocalDate.now();
        return false;
    }


    /**
     * 挂单
     * @param enableList
     * @throws Exception
     */
    @Override
    public void pendingEnableOrders(List<StockApplyConfig> enableList) throws Exception {
        var applyCodeList = new ArrayList<String>();
        for (var enableItem : enableList){
            //先买入
            var param = new TransactionParam(){{
                setCode(enableItem.getCode());
                setName(enableItem.getName());
                setCount(enableItem.getBuyCount());
                setPrice(enableItem.getLowPrice());
            }};
            var buyCode = brokerageService.buy(param);
            //后卖出
            param.setPrice(enableItem.getHighPrice());
            param.setCount(enableItem.getSellCount());
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
     * 开盘判断是否有过多高开或者过多低开的情况
     * @param list
     * @return
     */
    @Override
    public List<StockApplyConfig> CalPrice(List<StockConfigDo> list){
        var result = new ArrayList<StockApplyConfig>();
        for (var item : list){
            var resultItem = new StockApplyConfig();
            BeanUtils.copyProperties(item,resultItem);
            resultItem.setBuyCount(item.getCount());
            resultItem.setSellCount(item.getCount());
            var nowPrice = priceService.getLastPrice(resultItem.getCode());
            var oneCount = item.getCount();
            //如果比下一次买入的价格还低
            var nextBuyPrice = PriceUtil.getLowPrice(resultItem.getLowPrice());
            while (nowPrice.compareTo(nextBuyPrice)<=0){
                resultItem.setLowPrice(nextBuyPrice);
                resultItem.setBuyCount(resultItem.getBuyCount()+oneCount);
                nextBuyPrice = PriceUtil.getLowPrice(resultItem.getLowPrice());
            }
            var nextSellPrice = PriceUtil.getHighPrice(resultItem.getHighPrice());
            while (nowPrice.compareTo(nextSellPrice)>=0){
                resultItem.setHighPrice(nextSellPrice);
                resultItem.setSellCount(resultItem.getSellCount()+oneCount);
                nextSellPrice = PriceUtil.getHighPrice(resultItem.getHighPrice());
            }
            result.add(resultItem);
        }
        return result;
    }


    @Override
    public boolean removeApplyOrders() {
        return brokerageService.revokeOrders();
    }
}
