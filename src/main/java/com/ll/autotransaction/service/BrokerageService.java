package com.ll.autotransaction.service;

import com.ll.autotransaction.service.model.ApplyDataInfo;
import com.ll.autotransaction.service.model.DealInfo;
import com.ll.autotransaction.service.model.StockInfo;
import com.ll.autotransaction.service.model.TransactionParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BrokerageService {

    /**
     * 获取可用余额
     * @return
     */
    public BigDecimal getBalance();


    /**
     * 买入
     * @return
     */
    public String buy(TransactionParam param);


    /**
     * 卖出
     * @param param
     * @return
     */
    public String sell(TransactionParam param);

    /**
     * 撤单
     * @param applyCode
     * @return
     */
    public boolean RevokeOrders(String applyCode);


    /**
     * 查看当前持仓
     */
    public List<StockInfo> getStockList();


    /**
     * 查看委托
     */
    public List<ApplyDataInfo> getOrdersData(LocalDate startTime,LocalDate endTime);


    /**
     * 查询历史成交记录
     */
    public List<DealInfo>  getHisDealData(LocalDate startTime, LocalDate endTime);


    /**
     * 查看今日的委托
     * @return
     */
    public List<ApplyDataInfo> getTodayOrdersData();


    /**
     * 查看今日成交记录
     * @return
     */
    public List<DealInfo> getTodayHisDealData();


}
