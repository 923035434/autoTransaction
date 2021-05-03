package com.ll.autotransaction.controller.security;

import com.ll.autotransaction.service.UserService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultUserPrincipalService implements UserPrincipalService {


    @Autowired
    UserService userService;



    @Override
    public UserPrincipal loadUserByUsername(String username) {
        var userInfo = userService.getUserByAccount(username);
        if(userInfo==null){
            return null;
        }
        UserPrincipal principal = new UserPrincipal();
        principal.setUserId(userInfo.getId().toString());
        principal.setUsername(userInfo.getAccountNumber());
        principal.setEnabled(userInfo.getStatus()==0);
        principal.setAccountNonLocked(true);
        return principal;
    }

    @Override
    public void saveUser(UserPrincipal user) {

    }

    @Override
    public void refreshUserIfPresent(UserPrincipal user) {
    }

}
