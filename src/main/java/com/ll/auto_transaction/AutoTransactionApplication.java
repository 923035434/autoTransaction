package com.ll.auto_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class AutoTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoTransactionApplication.class, args);
    }

}
