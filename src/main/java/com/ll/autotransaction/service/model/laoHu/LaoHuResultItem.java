package com.ll.autotransaction.service.model.laoHu;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LaoHuResultItem {

    /**
     * 编码
     */
    private String symbol;

    /**
     * 名称
     */
    private String nameCN;


    /**
     * 最新价格
     */
    private BigDecimal latestPrice;

}
