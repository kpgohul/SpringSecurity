package com.gohul.springSecurity.filter;

import com.gohul.springSecurity.constants.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String JwtToken = request.getHeader(AppConstants.JWT_HEADER);
        if (JwtToken != null)
        {
            try {
                Environment environment = getEnvironment();
                String originalSecretKey = environment.getProperty(
                        AppConstants.JWT_SECRET_KEY,
                        AppConstants.JWT_SECRET_KEY_DEFAULT
                );
                SecretKey decodeSecretKey = Keys.hmacShaKeyFor(originalSecretKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser().verifyWith(decodeSecretKey).build()
                        .parseSignedClaims(JwtToken).getPayload();
                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null
                        , AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e)
            {
                throw new BadCredentialsException("Invalid Json Token");
            }
        }
        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/customers/register") ||
                request.getRequestURI().equals("/customers/customer") ||
                request.getRequestURI().equals("/customers/login");
    }
}
