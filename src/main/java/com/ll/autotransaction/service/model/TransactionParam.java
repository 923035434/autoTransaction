package com.ll.autotransaction.service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionParam {

    //股票代码
    private String code;

    //股票名称
    private String name;

    //股票数量
    private Integer count;

    //价格
    private BigDecimal price;

}
