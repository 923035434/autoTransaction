package com.ll.autotransaction.controller.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 用户认证信息存储，根据用户请求Token信息，获取用户认证信息
 */
@Slf4j
public class DefaultUserAuthenticationTokenRepository implements UserAuthenticationTokenRepository {

    private UserPrincipalService userPrincipalService;

    /**
     * Token加密密钥
     */
    private String secret;
    /**
     * Token有效期（秒）
     */
    private int expirationTime;

    public DefaultUserAuthenticationTokenRepository(String secret, int expirationTime, UserPrincipalService userPrincipalService) {
        Assert.hasText(secret, "Authentication token secret is empty");

        this.secret = secret;
        this.expirationTime = expirationTime;
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    public UserAuthenticationToken get(String requestToken) {
        if (!StringUtils.hasText(requestToken)) {
            return null;
        }

        try {

            // 判断当前用户的认证Token是否有效
            Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(requestToken).getBody();
            if (!validate(body)) {
                return null;
            }

            // 获取当前用户状态信息
            UserPrincipal userPrincipal = userPrincipalService.loadUserByUsername(body.getAudience());
            if (userPrincipal == null) {
                return null;
            }

            return new UserAuthenticationToken(userPrincipal);
            // TODO 停用，后续使用Filter统一处理权限验证
            // if (!userPrincipal.isEnabled() || !userPrincipal.isAccountNonLocked()) {
            // token.setAuthenticated(false);
            // }
        } catch (Exception e) {
            // 不抛出错误
            log.warn("获取用户认证信息异常", e);
        }

        return null;
    }

    @Override
    public String save(UserAuthenticationToken authenticationToken) {
        String jwtToken = getJwtToken(authenticationToken.getName(), authenticationToken);
        return jwtToken;
    }

    private String getJwtToken(String id, UserAuthenticationToken token) {
        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setId(id).setAudience(token.getName()).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    @Override
    public void remove(String requestToken) {

    }

    private boolean validate(Claims body) {
        if (body.getExpiration().before(new Date())) {
            return false;
        }

        return true;
    }
}
