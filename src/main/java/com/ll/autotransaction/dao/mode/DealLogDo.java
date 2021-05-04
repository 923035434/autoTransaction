package com.ll.autotransaction.dao.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("deal_log")
public class DealLogDo {

    private Integer id;

    private String code;

    private String name;

    private BigDecimal price;

    //(0:买入1:卖出)
    private int type;

    private int count;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
