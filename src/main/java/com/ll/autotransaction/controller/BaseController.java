package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {


    protected Integer getUserId(){
        Integer userId = null;
        UserPrincipal principal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal!=null){
            userId = Integer.valueOf(principal.getUserId());
        }
        return userId;
    }


}
