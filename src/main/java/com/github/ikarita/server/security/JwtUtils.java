package com.github.ikarita.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.ikarita.server.security.Permissions.ALLOWED_PATHS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtUtils {
    @Value("${com.github.ikarita.server.security.jwtSecret}")
    private String jwtSecret;
    @Value("${com.github.ikarita.server.security.jwtAccessExpirationMs}")
    private long jwtAccessExpirationMs;
    @Value("${com.github.ikarita.server.security.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    private Algorithm algorithm = null;

    public static boolean requiresValidation(HttpServletRequest request){
        return Arrays.stream(ALLOWED_PATHS).noneMatch(s -> request.getServletPath().equals(s));
    }

    public void validateJWT(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            if(isTokenMissing(request)){
                throw new IllegalStateException("Missing JWT");
            }

            final DecodedJWT token = decodeToken(extractToken(request));
            final String username = token.getSubject();
            final String[] roles = token.getClaim("roles").asArray(String.class);

            final List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error when processing JWT token: [{}] {}", e.getClass().getSimpleName(), e.getMessage());
            setForbiddenResponse(response, e.getMessage());
        }
    }

    public String createAccessToken(HttpServletRequest request, String username, List<String> roles){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", roles)
                .sign(algorithm());
    }

    public String createRefreshToken(HttpServletRequest request, String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm());
    }

    public DecodedJWT decodeToken(String token){
        final JWTVerifier verifier = JWT.require(algorithm()).build();
        return verifier.verify(token);
    }

    public static boolean isTokenMissing(HttpServletRequest request){
        return request.getHeader(AUTHORIZATION) == null
                || !request.getHeader(AUTHORIZATION).startsWith("Bearer ");
    }

    public static String extractToken(HttpServletRequest request){
        return request.getHeader(AUTHORIZATION).substring("Bearer ".length());
    }

    public static void setJwtResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        final Map<String, String> tokens = new HashMap<>(2);
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static void setForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        final Map<String, String> cause = new HashMap<>(1);
        cause.put("cause", message);
        new ObjectMapper().writeValue(response.getOutputStream(), cause);
    }

    private Algorithm algorithm(){
        if(this.algorithm == null){
            this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }

        return algorithm;
    }
}
