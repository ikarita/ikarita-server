package com.github.ikarita.server.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAlgorithm {
    @Value("${com.github.ikarita.server.security.jwtSecret}")
    private String jwtSecret;

    private Algorithm algorithm = null;
    Algorithm algorithm(){
        if(this.algorithm == null){
            this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }

        return algorithm;
    }

    DecodedJWT decodeToken(String token){
        final JWTVerifier verifier = JWT.require(algorithm()).build();
        return verifier.verify(token);
    }
}
