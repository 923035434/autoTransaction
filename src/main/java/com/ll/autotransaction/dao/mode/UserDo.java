package com.ll.autotransaction.dao.mode;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserDo {

    private Integer id;

    private LocalDateTime createTime;


    private LocalDateTime loginTime;


    private String accountNumber;


    private String password;

    private int status;

    private String cookies;


    private String validateCode;


    private String host;


    private int enableAutoTransaction;

}
