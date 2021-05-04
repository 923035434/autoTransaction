package com.ll.autotransaction.controller.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HomeResultVO {

    private Boolean apiState;


    private Boolean enableAutoTransaction;


    private BigDecimal balance;


    private BigDecimal todayProfit;


    private BigDecimal allProfit;



}
