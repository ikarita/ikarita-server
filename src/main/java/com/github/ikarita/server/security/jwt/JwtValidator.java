package com.github.ikarita.server.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.security.permissions.AllowedPaths;
import com.github.ikarita.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidator {
    private final JwtAlgorithm jwtAlgorithm;
    private final JwtGenerator jwtGenerator;

    public boolean requiresValidation(HttpServletRequest request){
        if(Arrays.stream(AllowedPaths.all()).anyMatch(s -> checkPath(request.getServletPath(), s))){
            return false;
        }

        if(request.getMethod().equals("POST") && Arrays.stream(AllowedPaths.post()).anyMatch(s -> checkPath(request.getServletPath(), s))){
            return false;
        }

        return Arrays.stream(AllowedPaths.swagger()).noneMatch(s -> checkPath(request.getServletPath(), s));
    }

    public void refresh(UserService userService, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String refresh_token = JwtUtils.extractToken(request);
        final DecodedJWT decodedToken = jwtAlgorithm.decodeToken(refresh_token);
        final String username = decodedToken.getSubject();
        final UserDto user = userService.getUser(username);
        final List<String> roles = user.getUserRole().getGrantedAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final String url = request.getRequestURL().toString();
        final String access_token = jwtGenerator.createAccessToken(url, username, roles);

        JwtUtils.setJwtResponse(response, access_token, refresh_token);
    }

    public boolean access(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(JwtUtils.isTokenMissing(request)){
            JwtUtils.setUnauthorizedResponse(response, "No bearer token passed in request.");
            return false;
        }

        try {
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
            return false;
        }

        return true;
    }

    private static boolean checkPath(String path, String pattern){
        if(pattern.endsWith("/**")){
            return path.startsWith(pattern.substring(0, pattern.length() - 3));
        }

        return path.equals(pattern);
    }
}
