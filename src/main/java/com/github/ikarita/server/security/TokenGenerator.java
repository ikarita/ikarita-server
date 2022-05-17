package com.github.ikarita.server.security;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenGenerator {
    @Value("${com.github.ikarita.server.security.jwtAccessExpirationMs}")
    private long jwtAccessExpirationMs;
    @Value("${com.github.ikarita.server.security.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    private final JwtAlgorithm jwtAlgorithm;

    public String createAccessToken(String url, String username, List<String> authorities){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .withIssuer(url)
                .withClaim("roles", authorities)
                .sign(jwtAlgorithm.algorithm());
    }

    public String createRefreshToken(String url, String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .withIssuer(url)
                .sign(jwtAlgorithm.algorithm());
    }

}
