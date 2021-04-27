package com.ll.auto_transaction.service;

import com.ll.auto_transaction.service.model.ApplyDataInfo;
import com.ll.auto_transaction.service.model.DealInfo;
import com.ll.auto_transaction.service.model.StockInfo;
import com.ll.auto_transaction.service.model.TransactionParam;

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
    public List<ApplyDataInfo> GetOrdersData(LocalDate startTime,LocalDate endTime);


    /**
     * 查询历史成交记录
     */
    public List<DealInfo>  GetHisDealData(LocalDate startTime, LocalDate endTime);


}
