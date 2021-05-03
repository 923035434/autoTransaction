package com.ll.autotransaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Repository;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class AutoTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoTransactionApplication.class, args);
    }

}
