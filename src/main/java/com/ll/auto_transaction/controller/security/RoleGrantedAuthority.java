package com.ll.auto_transaction.controller.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class RoleGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    private String role;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return role;
    }

}