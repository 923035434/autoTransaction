package com.ll.auto_transaction.controller;

import com.ll.auto_transaction.controller.model.CommonResult;
import com.ll.auto_transaction.service.model.UserInfo;
import lombok.var;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class HomeController {


    @GetMapping("/hello")
    public CommonResult<String> hello(){
        return CommonResult.success("hello");
    }




}
