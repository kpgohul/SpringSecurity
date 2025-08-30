package com.gohul.springSecurity.sucurityExcep;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errMsg = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Authentication failed";
        String path = request.getRequestURI();
        LocalDateTime dateTime = LocalDateTime.now();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("custom-authentication-error", "failed due to authentication");
        response.setContentType("application/json;charset=UTF-8");
        String errResBody = String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                dateTime, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                errMsg, path);
        response.getWriter().write(errResBody);

    }
}
