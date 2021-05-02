package com.ll.autotransaction;

import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AutoTransactionApplicationTests {


    @Test
    public void demoTest(){
        var price = BigDecimal.valueOf(56.35);
        var nowPrice = price;
        int count = 900;
        int sellCount = 100;
        int minCount = 200;
        var profit = BigDecimal.ZERO;
        while (count>minCount){
            nowPrice = nowPrice.multiply(BigDecimal.valueOf(1.031));
            profit =profit.add((nowPrice.subtract(price)).multiply(BigDecimal.valueOf(sellCount))) ;
            count-=sellCount;
        }
        System.out.println("最后价格："+nowPrice);
        System.out.println("盈利："+profit);

    }




}
