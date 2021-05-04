package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.model.CommonResult;
import com.ll.autotransaction.controller.model.vo.HomeResultVO;
import com.ll.autotransaction.controller.security.UserPrincipal;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.model.SystemConfigInfo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    BrokerageService brokerageService;


    @GetMapping("/getApiState")
    public CommonResult<HomeResultVO> getApiState() {
        var result = new HomeResultVO();
        var balance = brokerageService.getBalance();
        result.setApiState(true);
        result.setBalance(balance);
        var userInfo = userService.getUserById(getUserId());
        result.setEnableAutoTransaction(userInfo.getEnableAutoTransaction() == 1);

        return CommonResult.success(result);
    }


    @GetMapping("/EditSystemConfig")
    public CommonResult<Boolean> EditSystemConfig(@RequestBody SystemConfigInfo param) throws Exception {
        var result = userService.EditSystemConfig(getUserId(), param);
        return CommonResult.success(result);
    }


}
