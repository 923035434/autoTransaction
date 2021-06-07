package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.model.CommonResult;
import com.ll.autotransaction.controller.model.vo.HomeResultVO;
import com.ll.autotransaction.controller.model.vo.JobConfigVO;
import com.ll.autotransaction.controller.security.UserPrincipal;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.config.BrokerageConfig;
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
        userService.updateConfig();
        var result = new HomeResultVO();
        var accountInfo = brokerageService.getStockAccountInfo();
        result.setApiState(true);
        result.setBalance(accountInfo.getBalance());
        result.setTodayProfit(accountInfo.getTodayProfit());
        result.setAllProfit(accountInfo.getAllProfit());
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


    @PostMapping("/getCurrentJobConfig")
    public CommonResult<JobConfigVO> getCurrentJobConfig(){
        var result = new JobConfigVO(){{
            setDfcfCookies(BrokerageConfig.dfcfCookies);
            setDfcfHost(BrokerageConfig.dfcfHost);
            setDfcfValidateCode(BrokerageConfig.dfcfValidateCode);
            setEnableAutoTransaction(BrokerageConfig.enableAutoTransaction);
            setWindUpDate(BrokerageConfig.windUpDate);
            setPendingOrderDate(BrokerageConfig.pendingOrderDate);
            setApplyDataInfos(BrokerageConfig.applyDataInfos);
        }};
        return CommonResult.success(result);
    }


}
