package com.ll.autotransaction.util;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailUtil {


    @Autowired
    JavaMailSender javaMailSender;

    public boolean sent(String title,String content){
        var result = true;
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(title);
            message.setFrom("164884173@qq.com");
            message.setTo("923035434@qq.com");
            message.setSentDate(new Date());
            message.setText(content);
            javaMailSender.send(message);
        }catch (Exception e){
            result = false;
        }
        return result;
    }


}
