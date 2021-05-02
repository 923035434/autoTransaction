package com.ll.auto_transaction.dao.mode;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("investment.user")
public class UserDo {

    private int id;

    private LocalDateTime createTime;


    private LocalDateTime loginTime;


    private String accountNumber;


    private String password;

    private int status;


}
