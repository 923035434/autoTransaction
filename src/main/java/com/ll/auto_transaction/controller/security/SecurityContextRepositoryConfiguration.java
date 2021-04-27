package com.ll.auto_transaction.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableConfigurationProperties(SecurityContextRepositoryProperties.class)
public class SecurityContextRepositoryConfiguration {

    @Autowired
    private SecurityContextRepositoryProperties repositoryProperties;


    @Bean
    @Primary
    public SecurityContextRepository
    securityContextRepository(WebTokenValueResolver webTokenValueResolver,
                              UserAuthenticationTokenRepository userAuthenticationTokenRepository) {
        return new SecurityContextRepositoryWrapper(
                new DefaultSecurityContextRepository(webTokenValueResolver,
                        userAuthenticationTokenRepository));
    }

    @Bean
    public WebTokenValueResolver webTokenValueResolver() {
        return new CookieWebTokenValueResolver(repositoryProperties.getCookieName(),
                repositoryProperties.getCookieDomain(),
                repositoryProperties.getExpirationTime());

    }

    @Bean
    public UserAuthenticationTokenRepository userAuthenticationTokenRepository(
            UserPrincipalService userPrincipalService) {
        return new
                DefaultUserAuthenticationTokenRepository(repositoryProperties.getSecret(),
                repositoryProperties.getExpirationTime(),
                userPrincipalService);
    }

    @Bean
    public UserPrincipalService userPrincipalService() {
        return new DefaultUserPrincipalService();
    }


}

