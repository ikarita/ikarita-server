package com.github.ikarita.server.api;

import com.github.ikarita.server.security.JwtUtils;
import com.github.ikarita.server.security.TokenValidator;
import com.github.ikarita.server.service.UserService;
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
    private final TokenValidator tokenValidator;

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            if(JwtUtils.isTokenMissing(request)){
                throw new IllegalStateException("Refresh token is missing");
            }

            tokenValidator.refresh(userService, request, response);
        }catch (Exception e){
            JwtUtils.setForbiddenResponse(response, e.getMessage());
        }
    }
}
