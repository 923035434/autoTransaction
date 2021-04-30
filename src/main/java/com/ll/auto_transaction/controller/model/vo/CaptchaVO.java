package com.ll.auto_transaction.controller.model.vo;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CaptchaVO {

    //图形验证码uuid
    private String uuid;

    //验证码图片Base64格式
    private String imageBase64;

}
