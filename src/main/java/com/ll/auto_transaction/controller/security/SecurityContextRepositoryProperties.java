package com.ll.auto_transaction.controller.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "web.security.repository")
public class SecurityContextRepositoryProperties {
    /**
     * Token加密密钥
     */
    private String secret;
    /**
     * 用户Token Cookie名称
     */
    private String cookieName;
    /**
     * 用户Token Cookie域名
     */
    private String cookieDomain;
    /**
     * 用户Token有效期（秒）
     */
    private Integer expirationTime;
    /**
     * 用户认证信息存储键值前缀
     */
    private String redisKeyPrefix;
}
