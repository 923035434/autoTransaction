package com.ll.autotransaction;

import com.ll.autotransaction.util.PriceUtil;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AutoTransactionApplicationTests {


    @Test
    public void demoTest(){
        var str = "9:30";
        var strArray = Arrays.asList(str.split(":"));
        if(strArray.size()>0){
            for (int i=0;i<strArray.size();i++){
                var strItem = strArray.get(i);
                if(strItem.length()<2){
                    strItem = "0"+strItem;
                    strArray.set(i,strItem);
                }
            }
            str = strArray.stream().collect(Collectors.joining(":"));
        }
        System.out.println(str);
    }




}
