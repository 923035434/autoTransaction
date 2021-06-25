package com.ll.autotransaction.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockApplyConfig {

    private Integer id;

    private Integer enable;

    private String code;

    private String name;

    private BigDecimal price;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private Integer buyCount;

    private Integer sellCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
