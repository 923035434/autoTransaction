package com.ll.autotransaction.service;

import com.ll.autotransaction.service.model.*;

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
     * 获取资金数据
     * @return
     */
    public StockAccountInfo getStockAccountInfo();



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
    public boolean revokeOrders(String applyCode);

    /**
     * 撤单
     * @return
     */
    public boolean revokeOrders();

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
    public List<ApplyDataInfo> getTodayOrdersData(String state);


    /**
     * 查看今日成交记录
     * @return
     */
    public List<DealInfo> getTodayHisDealData();


}
