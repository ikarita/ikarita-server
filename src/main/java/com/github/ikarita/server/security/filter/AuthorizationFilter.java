package com.github.ikarita.server.security.filter;

import com.github.ikarita.server.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.ikarita.server.security.JwtUtils.requiresValidation;

@Slf4j
@AllArgsConstructor
@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requiresValidation(request)) {
            jwtUtils.validateJWT(request, response, filterChain);
        }

        filterChain.doFilter(request, response);
    }
}
