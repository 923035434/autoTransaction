package com.ll.autotransaction.controller.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
public class CookieWebTokenValueResolver implements WebTokenValueResolver {
    private String name;
    private String domain;
    private int maxAge = 1440;
    private Boolean useSecureCookie;

    public CookieWebTokenValueResolver(String name, String domain, int maxAge) {
        Assert.hasText(name, "Cookie name is empty");
        Assert.isTrue(maxAge > 0, "Cookie maxAge invalid");

        this.name = name;
        this.domain = domain;
        this.maxAge = maxAge;
    }

    @Override
    public String getWebTokenValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if ((cookies == null) || (cookies.length == 0)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    public void setWebTokenValue(String tokenValue, HttpServletRequest request,
                                 HttpServletResponse response) {
        Cookie cookie = new Cookie(name, tokenValue);
        cookie.setPath(getCookiePath(request));
        if (domain != null) {
            cookie.setDomain(domain);
        }

        cookie.setMaxAge(maxAge);

        if (maxAge < 1) {
            cookie.setVersion(1);
        }

        if (useSecureCookie == null) {
            cookie.setSecure(request.isSecure());
        } else {
            cookie.setSecure(useSecureCookie);
        }

        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    @Override
    public void expireWebTokenValue(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        if (domain != null) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        // String contextPath = request.getContextPath();
        // return contextPath.length() > 0 ? contextPath : "/";
        return "/";
    }

    public void setCookieDomain(String cookieDomain) {
        this.domain = cookieDomain;
    }

    public void setUseSecureCookie(Boolean useSecureCookie) {
        this.useSecureCookie = useSecureCookie;
    }

    public void setCookieName(String cookieName) {
        this.name = cookieName;
    }

}
