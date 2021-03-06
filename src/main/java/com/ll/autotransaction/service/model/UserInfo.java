package com.ll.autotransaction.service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {


    /**
     * 用户ID
     */
    private Integer id;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 密码
     */
    private String password;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 状态（0：正常；1：禁用）
     */
    private Integer status;


    private String cookies;


    private String validateCode;


    private String host;


    private int enableAutoTransaction;



}
