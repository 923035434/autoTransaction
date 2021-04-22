package com.ll.auto_transaction.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ApplyDataInfo {

    //委托编号
    private String applyCode;

    //委托时间
    private LocalDateTime applyTime;

    //股票代码
    private String code;

    //股票名称
    private String name;

    //委托类型  配售申购  证券买入 证券卖出
    private String applyType;

    //价格
    private BigDecimal price;

    //委托数量
    private int count;

    //成交数量
    private int dealCount;

    //委托状态  已报  已撤 已成
    private String status;


}
