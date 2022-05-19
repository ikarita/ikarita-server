package com.github.ikarita.server.api;

import com.github.ikarita.server.security.*;
import com.github.ikarita.server.security.jwt.JwtAlgorithm;
import com.github.ikarita.server.security.jwt.JwtGenerator;
import com.github.ikarita.server.security.jwt.JwtValidator;
import com.github.ikarita.server.security.permissions.UserRole;
import com.github.ikarita.server.service.user.LocalUserDetailsService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest
@ContextConfiguration(classes = {SecurityConfiguration.class, JwtGenerator.class, JwtValidator.class, JwtAlgorithm.class})
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JwtGenerator jwtGenerator;

    @MockBean
    private LocalUserDetailsService userDetailsService;

    protected String authenticateUser(User user){
        final List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final String token = "Bearer " + jwtGenerator.createAccessToken("test.com", user.getUsername(), authorities);
        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        return token;
    }

    protected ResultActions performAuthenticated(MockHttpServletRequestBuilder request, User user) throws Exception {
        return mockMvc.perform(request.header("Authorization", authenticateUser(user)));
    }

    protected User viewer(){
        return user(UserRole.VIEWER.getGrantedAuthorities());
    }

    protected User contributor() {
        return user(UserRole.CONTRIBUTOR.getGrantedAuthorities());
    }

    protected User moderator() {
        return user(UserRole.MODERATOR.getGrantedAuthorities());
    }

    protected User admin(){
        return user(UserRole.ADMIN.getGrantedAuthorities());
    }

    protected byte[] toJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(dto);
    }

    private User user(Set<SimpleGrantedAuthority> authorities){
        return new User("user", "password", authorities);
    }
}
