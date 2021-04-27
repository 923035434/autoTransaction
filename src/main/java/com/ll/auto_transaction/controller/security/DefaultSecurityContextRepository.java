package com.ll.auto_transaction.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class DefaultSecurityContextRepository implements SecurityContextRepository {

    private WebTokenValueResolver webTokenValueResolver;
    private UserAuthenticationTokenRepository userAuthenticationTokenRepository;

    public DefaultSecurityContextRepository(WebTokenValueResolver webTokenValueResolver,
                                            UserAuthenticationTokenRepository userAuthenticationTokenRepository) {
        this.webTokenValueResolver = webTokenValueResolver;
        this.userAuthenticationTokenRepository = userAuthenticationTokenRepository;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        String requestToken = getUserRequestToken(request);

        if (StringUtils.hasText(requestToken)) {
            UserAuthenticationToken authenticationToken =
                    userAuthenticationTokenRepository.get(requestToken);
            if (authenticationToken != null) {
                context.setAuthentication(authenticationToken);
            }
        }

//         测试用户
//         if (context.getAuthentication() == null) {
//         UserPrincipal userPrincipal = new UserPrincipal();
//         userPrincipal.setEnableMultiClients(true);
//         userPrincipal.setAccountNonLocked(true);
//         userPrincipal.setUserId("0");
//         userPrincipal.setUsername("test_user");
//         userPrincipal.setEnabled(true);
//         context.setAuthentication(new UserAuthenticationToken(userPrincipal));
//         }

        return context;
    }

    private String getUserRequestToken(HttpServletRequest request) {
        // 方便Swagger UI测试
        String token = request.getHeader("Authentication");
        if (StringUtils.hasText(token)) {
            return token;
        }
        return webTokenValueResolver.getWebTokenValue(request);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request,
                            HttpServletResponse response) {
        if (context == null || !(context.getAuthentication() instanceof UserAuthenticationToken)) {
            if (log.isDebugEnabled()) {
                log.debug("SecurityContext is empty or not UserAuthenticationToken.");
            }

            webTokenValueResolver.expireWebTokenValue(request, response);
            String requestToken = getUserRequestToken(request);
            if (StringUtils.hasText(requestToken)) {
                userAuthenticationTokenRepository.remove(requestToken);
            }
            return;
        }

        Authentication authentication = context.getAuthentication();

        UserAuthenticationToken authenticationToken = (UserAuthenticationToken)authentication;
        String requestToken = userAuthenticationTokenRepository.save(authenticationToken);
        // 缓存JWT Token
        webTokenValueResolver.setWebTokenValue(requestToken, request, response);

        if (log.isDebugEnabled()) {
            log.debug("SecurityContext '" + authenticationToken.getName() + "' stored to Redis: '"
                    + requestToken);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return StringUtils.hasText(getUserRequestToken(request));
    }

}