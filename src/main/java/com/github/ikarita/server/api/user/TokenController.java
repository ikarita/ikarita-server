package com.github.ikarita.server.api.user;

import com.github.ikarita.server.security.jwt.JwtUtils;
import com.github.ikarita.server.security.jwt.JwtValidator;
import com.github.ikarita.server.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {
    private final UserService userService;
    private final JwtValidator jwtValidator;

    @PostMapping(path = "/refresh", produces = "application/json")
    @Operation(
            tags = {"Tokens"},
            summary = "Refreshes access token given refresh token is still valid."
    )
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            if(JwtUtils.isTokenMissing(request)){
                throw new IllegalStateException("Refresh token is missing");
            }

            jwtValidator.refresh(userService, request, response);
        }catch (Exception e){
            JwtUtils.setForbiddenResponse(response, e.getMessage());
        }
    }
}
