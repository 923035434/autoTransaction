package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.model.CommonResult;
import com.ll.autotransaction.controller.model.vo.HomeResultVO;
import com.ll.autotransaction.controller.security.UserPrincipal;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.model.SystemConfigInfo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    BrokerageService brokerageService;


    @PostMapping("/getHomeResult")
    public CommonResult<HomeResultVO> getHomeResult() {
        var result = new HomeResultVO();
//        var balance = brokerageService.getBalance();
//        result.setApiState(true);
//        result.setBalance(balance);
        result.setApiState(true);
        result.setBalance(BigDecimal.valueOf(10000));
        result.setTodayProfit(BigDecimal.valueOf(6666));
        result.setAllProfit(BigDecimal.valueOf(8888));
        var userInfo = userService.getUserById(getUserId());
        result.setEnableAutoTransaction(userInfo.getEnableAutoTransaction() == 1);
        return CommonResult.success(result);
    }


    @PostMapping("/editSystemConfig")
    public CommonResult<Boolean> editSystemConfig(@RequestBody SystemConfigInfo param) throws Exception {
        var result = userService.editSystemConfig(getUserId(), param);
        return CommonResult.success(result);
    }


    @PostMapping("/getSystemConfig")
    public CommonResult<SystemConfigInfo> getSystemConfig() throws Exception {
        var result = userService.getSystemConfig(getUserId());
        return CommonResult.success(result);
    }


}
