package com.ll.autotransaction.controller.security;

/**
 * 用户认证信息状态管理接口
 */
public interface UserPrincipalService {
    /**
     * 获取用户认证状态信息
     *
     * @param username 用户名
     * @return 用户认证状态信息
     */
    UserPrincipal loadUserByUsername(String username);

    /**
     * 保存用户认证状态信息
     *
     * @param user 用户认证状态信息
     */
    void saveUser(UserPrincipal user);

    /**
     * 更新户认证状态信息
     *
     * @param user 用户认证状态信息
     */
    void refreshUserIfPresent(UserPrincipal user);
}