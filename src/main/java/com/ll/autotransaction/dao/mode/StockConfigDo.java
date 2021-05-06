package com.ll.autotransaction.dao.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("stock_config")
public class StockConfigDo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer enable;

    private String code;

    private String name;

    private BigDecimal price;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private Integer count;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
