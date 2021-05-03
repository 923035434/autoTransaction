package com.ll.autotransaction.controller.model.vo;

import lombok.Data;

@Data
public class LoginParamVO {

    private String accountNumber;


    private String password;


    //图形验证码uuid
    private String uuid;

    //验证码
    private String validateCode;


}
