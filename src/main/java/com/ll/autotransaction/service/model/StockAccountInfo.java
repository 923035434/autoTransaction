package com.ll.autotransaction.service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockAccountInfo {

    private BigDecimal balance;


    private BigDecimal todayProfit;


    private BigDecimal allProfit;

}
