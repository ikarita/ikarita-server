package com.github.ikarita.server.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtUtils {
    private JwtUtils() {}

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
        setResponseStatus(FORBIDDEN, response, message);
    }

    public static void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        setResponseStatus(UNAUTHORIZED, response, message);
    }

    public static void setResponseStatus(HttpStatus status, HttpServletResponse response, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        final Map<String, String> cause = new HashMap<>(1);
        cause.put("cause", message);
        new ObjectMapper().writeValue(response.getOutputStream(), cause);
    }
}
