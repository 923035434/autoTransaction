package com.ll.auto_transaction.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.auto_transaction.controller.model.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public AuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws ServletException, IOException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeToWeb(response);
        } else {
            super.commence(request, response, authException);
        }
    }

    public static void writeToWeb(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(CommonResult.error(HttpStatus.UNAUTHORIZED,"登录失效请重新登录")));
        out.flush();
        out.close();
    }

}
