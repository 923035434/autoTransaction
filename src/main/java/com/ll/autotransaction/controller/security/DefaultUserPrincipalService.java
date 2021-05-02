package com.ll.autotransaction.controller.security;

public class DefaultUserPrincipalService implements UserPrincipalService {



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
