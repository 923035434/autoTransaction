package com.ll.auto_transaction.controller.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class UserAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 1L;

    @Getter
    private UserPrincipal principal;

    @Getter
    @Setter
    private boolean authenticated;

    public UserAuthenticationToken(UserPrincipal userPrincipal) {
        this.principal = userPrincipal;
        if (!userPrincipal.isEnabled() || !userPrincipal.isAccountNonLocked()) {
            this.authenticated = false;
        } else {
            this.authenticated = true;
        }
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }

    @Override
    public Collection<RoleGrantedAuthority> getAuthorities() {
        return principal.getAuthorities();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}