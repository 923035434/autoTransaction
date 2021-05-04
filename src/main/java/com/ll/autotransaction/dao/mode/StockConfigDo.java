package com.ll.autotransaction.dao.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("stock_config")
public class StockConfigDo {

    private Integer id;

    private int enable;

    private String code;

    private String name;

    private BigDecimal price;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private int count;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
