package com.github.ikarita.server.security.filter;

import com.github.ikarita.server.security.TokenValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenValidator tokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isValid = true;

        if (tokenValidator.requiresValidation(request)) {
            isValid = tokenValidator.access(request, response);
        }

        if(isValid){
            filterChain.doFilter(request, response);
        }
    }
}
