package com.ll.auto_transaction.controller.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebTokenValueResolver {

    String getWebTokenValue(HttpServletRequest request);

    void setWebTokenValue(String tokenValue, HttpServletRequest request, HttpServletResponse response);

    void expireWebTokenValue(HttpServletRequest request, HttpServletResponse response);
}
