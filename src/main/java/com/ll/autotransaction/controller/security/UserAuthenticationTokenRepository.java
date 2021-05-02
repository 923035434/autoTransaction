package com.ll.autotransaction.controller.security;

/**
 * 用户认证信息存储
 */
public interface UserAuthenticationTokenRepository {
    /**
     * 根据前端Token返回用户认证信息
     */
    UserAuthenticationToken get(String requestToken);

    String save(UserAuthenticationToken authenticationToken);

    void remove(String requestToken);
}

