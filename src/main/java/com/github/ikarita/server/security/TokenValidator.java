package com.github.ikarita.server.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.CommunityRole;
import com.github.ikarita.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.ikarita.server.security.Permissions.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenValidator {
    private final JwtAlgorithm jwtAlgorithm;
    private final TokenGenerator tokenGenerator;

    public boolean requiresValidation(HttpServletRequest request){
        if(Arrays.stream(ALLOWED_PATHS).anyMatch(s -> checkPath(request.getServletPath(), s))){
            return false;
        }

        if(request.getMethod().equals("POST") && Arrays.stream(ALLOWED_POST_PATHS).anyMatch(s -> checkPath(request.getServletPath(), s))){
            return false;
        }

        if(Arrays.stream(SWAGGER_PATHS).anyMatch(s -> checkPath(request.getServletPath(), s))){
            return false;
        }

        return true;
    }

    public void refresh(UserService userService, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String refresh_token = JwtUtils.extractToken(request);
        final DecodedJWT decodedToken = jwtAlgorithm.decodeToken(refresh_token);
        final String username = decodedToken.getSubject();
        final UserDto user = userService.getUser(username);
        final List<String> roles = user.getCommunityRoles().stream()
                .map(CommunityRole::getName)
                .collect(Collectors.toList());

        final String url = request.getRequestURL().toString();
        final String access_token = tokenGenerator.createAccessToken(url, username, roles);

        JwtUtils.setJwtResponse(response, access_token, refresh_token);
    }

    public void access(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if(JwtUtils.isTokenMissing(request)){
                throw new IllegalStateException("Missing JWT");
            }

            final DecodedJWT token = jwtAlgorithm.decodeToken(JwtUtils.extractToken(request));
            final String username = token.getSubject();
            final String[] roles = token.getClaim("roles").asArray(String.class);

            final List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            log.error("Error when processing JWT token: [{}] {}", e.getClass().getSimpleName(), e.getMessage());
            JwtUtils.setForbiddenResponse(response, e.getMessage());
        }
    }

    private static boolean checkPath(String path, String pattern){
        if(pattern.endsWith("/**")){
            return path.startsWith(pattern.substring(0, pattern.length() - 3));
        }

        return path.equals(pattern);
    }
}
