package com.ll.auto_transaction.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DealInfo {

    //委托编号
    private String applyCode;

    //成交日期
    private LocalDateTime dealTime;

    //股票代码
    private String code;

    //股票名称
    private String name;

    //委托类型
    private String applyType;

    //成交数量
    private int count;

    //价格
    private BigDecimal price;

}
