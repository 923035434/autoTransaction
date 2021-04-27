package com.ll.auto_transaction.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
public class DefaultWebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Autowired(required = false)
    private SecurityContextRepository securityContextRepository;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // system
                .antMatchers("/error")
                .antMatchers("/actuator/**")
                .antMatchers("/system/**")
                // .antMatchers("/auth/**")
                .antMatchers("/favicon.ico")
                // swagger
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/images/**")
                .antMatchers("/webjars/**")
                .antMatchers("/v2/api-docs");

        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                // .antMatchers(securityProperties.getLogoutUrl()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .disable()
                .cors().and()
                .httpBasic().disable()
                .sessionManagement().disable()
                .csrf().disable();

        if (securityContextRepository != null) {
            http.securityContext().securityContextRepository(securityContextRepository);
        }

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置密码为明文
        // auth.userDetailsService(userDetailsServiceImpl()).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

}
