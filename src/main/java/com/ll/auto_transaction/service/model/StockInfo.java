package com.ll.auto_transaction.service.model;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StockInfo {

    //股票代码
    private String code;

    //股票名称
    private String name;


    //持仓数量
    private int count;

    //可用数量
    private int availableCount;


    //成本
    private BigDecimal cost;


    //当前价
    private BigDecimal nowPrice;


}
