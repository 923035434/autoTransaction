package com.ll.autotransaction;

import com.ll.autotransaction.util.PriceUtil;
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
        System.out.println(BigDecimal.valueOf(2.222).setScale(2, BigDecimal.ROUND_DOWN));
        System.out.println(BigDecimal.valueOf(2.222).setScale(2, BigDecimal.ROUND_UP));
    }




}
