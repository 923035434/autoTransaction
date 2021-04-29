package com.ll.auto_transaction;


import com.ll.auto_transaction.service.BrokerageService;
import com.ll.auto_transaction.service.impl.DfcfBrokerageServiceImpl;
import com.ll.auto_transaction.service.model.TransactionParam;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DfcfServiceTest {

    @Autowired
    private BrokerageService brokerageService;

    @Test
    public void getBalanceTest(){
        var result = brokerageService.getBalance();
        System.out.println(result);
    }

    @Test
    public void getStockListTest(){
        var result = brokerageService.getStockList();
        System.out.println(result);
    }


    @Test
    public void GetOrdersDataTest(){
        var result = brokerageService.getOrdersData(LocalDate.of(2021,4,29),LocalDate.of(2021,4,29));
        System.out.println(result);
    }


    @Test
    public void buyTest(){
//        var param = new TransactionParam(){{
//           setCode("600703");
//           setName("三安光电");
//           setCount(BigDecimal.valueOf(100));
//           setPrice(BigDecimal.valueOf(20.00));
//        }};
//        var result = brokerageService.buy(param);
//        System.out.println(result);
    }


    @Test
    public void SellTest(){
//        var param = new TransactionParam(){{
//            setCode("600703");
//            setName("三安光电");
//            setCount(BigDecimal.valueOf(100));
//            setPrice(BigDecimal.valueOf(29.00));
//        }};
//        var result = brokerageService.sell(param);
//        System.out.println(result);
    }



    @Test
    public void RevokeOrdersTest(){
        var result = brokerageService.RevokeOrders("5678");
        System.out.println(result);
    }


    @Test
    public void demoTest(){
        var time = LocalTime.of(9,30);
        System.out.println(time.isAfter(LocalTime.of(9,20,0))&&time.isBefore(LocalTime.of(9,30,0)));
    }


    @Test
    public void getTodayOrdersDataTest(){
        var result = brokerageService.getTodayOrdersData();
        System.out.println(result);
    }




}
