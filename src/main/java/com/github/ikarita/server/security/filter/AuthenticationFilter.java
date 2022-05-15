package com.github.ikarita.server.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ikarita.server.model.dto.UserLoginDto;
import com.github.ikarita.server.security.JwtUtils;
import com.github.ikarita.server.security.TokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final UserLoginDto userLogin = mapper.readValue(request.getInputStream(), UserLoginDto.class);

            username = userLogin.getUsername();
            password = userLogin.getPassword();
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("Authentication attempt with username '{}' and password '{}'", username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        final User user = (User)authentication.getPrincipal();
        final List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final String url = request.getRequestURL().toString();
        final String access_token = tokenGenerator.createAccessToken(url, user.getUsername(), roles);
        final String refresh_token = tokenGenerator.createRefreshToken(url, user.getUsername());

        JwtUtils.setJwtResponse(response, access_token, refresh_token);
    }
}
