package com.ll.autotransaction;


import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.model.TransactionParam;
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
//
    @Autowired
    private BrokerageService brokerageService;


    @Autowired
    private UserService userService;
//
//    @Test
//    public void getBalanceTest(){
//        var result = brokerageService.getBalance();
//        System.out.println(result);
//    }
//
//    @Test
//    public void getStockListTest(){
//        var result = brokerageService.getStockList();
//        System.out.println(result);
//    }
//
//
//    @Test
//    public void GetOrdersDataTest(){
//        var result = brokerageService.getOrdersData(LocalDate.of(2021,4,29),LocalDate.of(2021,4,29));
//        System.out.println(result);
//    }
//
//
//    @Test
//    public void buyTest(){
////        var param = new TransactionParam(){{
////           setCode("600703");
////           setName("三安光电");
////           setCount(BigDecimal.valueOf(100));
////           setPrice(BigDecimal.valueOf(20.00));
////        }};
////        var result = brokerageService.buy(param);
////        System.out.println(result);
//    }
//
//
//    @Test
//    public void SellTest(){
//        userService.updateConfig();
//        var param = new TransactionParam(){{
//            setCode("002044");
//            setName("美年健康");
//            setCount(100);
//            setPrice(BigDecimal.valueOf(12));
//        }};
//        var result = brokerageService.sell(param);
//        System.out.println(result);
//    }
//
//
//
//    @Test
//    public void RevokeOrdersTest(){
//        var result = brokerageService.revokeOrders("539172");
//        System.out.println(result);
//    }
//
//
//    @Test
//    public void demoTest(){
//        var time = LocalTime.of(9,30);
//        System.out.println(time.isAfter(LocalTime.of(9,20,0))&&time.isBefore(LocalTime.of(9,30,0)));
//    }
//
//
//    @Test
//    public void getTodayOrdersDataTest(){
//        var result = brokerageService.getTodayOrdersData(null);
//        System.out.println(result);
//    }
//
//
//    @Test
//    public void getTodayHisDealDataTest(){
//        var result = brokerageService.getTodayHisDealData();
//        System.out.println(result);
//    }
//


}
