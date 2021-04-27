package com.ll.auto_transaction.controller.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
public class HeaderWebTokenValueResolver implements WebTokenValueResolver {
    // public static final String WEB_TOKEN_EXPIRES_HEADER_NAME = "Web-Token-Expires";

    private String headerName;

    // @Getter(value = AccessLevel.PROTECTED)
    // @Setter
    // public String expiresHeaderName = WEB_TOKEN_HEADER_NAME;

    public HeaderWebTokenValueResolver(String headerName) {
        Assert.hasText(headerName, "Header name is empty");
        this.headerName = headerName;
    }

    @Override
    public String getWebTokenValue(HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    @Override
    public void setWebTokenValue(String tokenValue, HttpServletRequest request,
                                 HttpServletResponse response) {
        response.setHeader(headerName, tokenValue);
        // response.setHeader(expiresHeaderName, String.valueOf(expiresAt.getTime()));
    }

    @Override
    public void expireWebTokenValue(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, "");
        // response.setHeader(expiresHeaderName, "-1");
    }
}
