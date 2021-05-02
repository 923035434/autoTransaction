package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.model.CommonResult;
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
