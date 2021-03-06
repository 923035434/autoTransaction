package com.ll.autotransaction.util;

import java.math.BigDecimal;

public class PriceUtil {

    public static BigDecimal getLowPrice(BigDecimal price) {
//        return price.multiply(BigDecimal.valueOf(1 - 0.03)).setScale(2, BigDecimal.ROUND_DOWN);
        return price.multiply(BigDecimal.valueOf(1 - 0.03)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal getHighPrice(BigDecimal price) {
//        return price.multiply(BigDecimal.valueOf(1+0.031)).setScale(2, BigDecimal.ROUND_UP);
        return price.multiply(BigDecimal.valueOf(1 + 0.031)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


}
