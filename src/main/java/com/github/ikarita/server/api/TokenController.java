package com.github.ikarita.server.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.CommunityRole;
import com.github.ikarita.server.security.JwtUtils;
import com.github.ikarita.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            if(JwtUtils.isTokenMissing(request)){
                throw new IllegalStateException("Refresh token is missing");
            }

            final String refresh_token = JwtUtils.extractToken(request);
            final DecodedJWT decodedToken = jwtUtils.decodeToken(refresh_token);
            final String username = decodedToken.getSubject();
            final UserDto user = userService.getUser(username);
            final List<String> roles = user.getCommunityRoles().stream()
                    .map(CommunityRole::getName)
                    .collect(Collectors.toList());

            final String url = request.getRequestURL().toString();
            final String access_token = jwtUtils.createAccessToken(url, username, roles);

            JwtUtils.setJwtResponse(response, access_token, refresh_token);
        }catch (Exception e){
            JwtUtils.setForbiddenResponse(response, e.getMessage());
        }
    }
}
