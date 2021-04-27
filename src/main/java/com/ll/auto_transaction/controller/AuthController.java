package com.ll.auto_transaction.controller;

import com.ll.auto_transaction.controller.model.CommonResult;
import com.ll.auto_transaction.controller.security.UserAuthenticationToken;
import com.ll.auto_transaction.controller.security.UserPrincipal;
import com.ll.auto_transaction.service.model.UserInfo;
import lombok.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @GetMapping("/login")
    public CommonResult<Boolean> login(){
        var userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setAccountNumber("liuli");
        setResponseAuthInfo(userInfo);
        return CommonResult.success(true);
    }






    /**
     * 设置验证信息
     * @param userInfo
     */
    private void setResponseAuthInfo(UserInfo userInfo){
        UserPrincipal principal = new UserPrincipal();
        principal.setUserId(userInfo.getId().toString());
        principal.setUsername(userInfo.getAccountNumber());
        principal.setEnabled(true);
        principal.setAccountNonLocked(true);
        SecurityContextHolder.getContext().setAuthentication(new UserAuthenticationToken(principal));
    }



}
