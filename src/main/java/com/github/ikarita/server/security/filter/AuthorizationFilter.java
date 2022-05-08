package com.github.ikarita.server.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.ikarita.server.security.JwtUtils.requiresValidation;
import static com.github.ikarita.server.security.JwtUtils.validateJWT;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requiresValidation(request)) {
            validateJWT(request, response, filterChain);
        }

        filterChain.doFilter(request, response);
    }
}
