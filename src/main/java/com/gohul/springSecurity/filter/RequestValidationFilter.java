package com.gohul.springSecurity.filter;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class RequestValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) throw new BadCredentialsException("Authentication failed due to empty credentials");
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, "Basic "))
            throw new BadCredentialsException("Authentication failed due to the 'Basic' term not included in the request");
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decodedToken;
        try {
            decodedToken = Base64.getDecoder().decode(base64Token);
            String token = new String(decodedToken, StandardCharsets.UTF_8);
            int denim = token.indexOf(':');
            if (!token.contains(":") || denim == -1)
                throw new BadCredentialsException("Authentication failed due Authorization Basic request not contain ':' ! ");
            String email = token.substring(0, denim);
            if (email.toLowerCase().contains("invalid")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid email in credentials. (!avoid to use 'invalid' word with your mail')\"}");
                response.getWriter().flush();
                return;
            }
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Authentication failed to decode the request");
        }
        filterChain.doFilter(request, response);
    }
}
