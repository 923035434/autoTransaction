package com.ll.auto_transaction.controller.security;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultUserPrincipalService implements UserPrincipalService {


    public DefaultUserPrincipalService() {

    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        UserPrincipal principal = new UserPrincipal();
        principal.setUserId("1");
        principal.setUsername(username);
        principal.setEnabled(true);
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
