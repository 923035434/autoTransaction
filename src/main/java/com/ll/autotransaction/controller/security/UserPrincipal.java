package com.ll.autotransaction.controller.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 用户认证状态信息
 */
@Data
public class UserPrincipal implements  UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户登录名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String realname;
    /**
     * 用户未被锁定
     */
    private boolean accountNonLocked;
    /**
     * 用户是否有效
     */
    private boolean enabled;
    /**
     * 是否允许多客户端同时登录
     */
    private boolean enableMultiClients;
    /**
     * 用户拥有角色信息
     */
    private List<RoleGrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

}
