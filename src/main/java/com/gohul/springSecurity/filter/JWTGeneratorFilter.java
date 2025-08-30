package com.gohul.springSecurity.filter;

import com.gohul.springSecurity.constants.AppConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Environment environment = getEnvironment();
            String originalSecretKey = environment.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_KEY_DEFAULT);
            SecretKey decodedSecretKey = Keys.hmacShaKeyFor(originalSecretKey.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().issuer("Gohul's First Application").subject("Jwt_Token")
                    .claim("username",authentication.getName())
                    .claim("authorities", authentication.getAuthorities()
                    .stream().map(auth -> auth.getAuthority())
                    .collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime()+30000000000L))
                    .signWith(decodedSecretKey)
                    .compact();
            response.setHeader(AppConstants.JWT_HEADER, jwt);

        }
        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/customers/login");
    }


}
