package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.security.JwtUtils;
import com.github.ikarita.server.service.CommunityService;
import com.github.ikarita.server.service.LocalUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommunityController.class)
@ContextConfiguration(classes = {JwtUtils.class})
class CommunityControllerTest {
    @MockBean
    private CommunityService communityService;

    @MockBean
    private LocalUserDetailsService userDetailsService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Test
    void testGetCommunitiesWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities")).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities/1"))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    void testGetCommunitiesWithAuthenticationIsUnauthorized() throws Exception {
//        final User user = new User("user", "user@gmail.com", Collections.emptySet());
//        final String tokenValue = "Bearer " + jwtUtils.createAccessToken("test.com", user.getUsername(), Collections.emptyList());
//
//        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);
//        Mockito.when(communityService.getCommunities()).thenReturn(communityDtoList());
//
//        mockMvc.perform(get("/api/v1/communities").header("Authorization", tokenValue)).andExpect(status().isOk());
//    }

    private static List<CommunityDto> communityDtoList(){
        final CommunityDto community1 = new CommunityDto(
                1L,
                "Community 1",
                true
        );

        final CommunityDto community2 = new CommunityDto(
                2L,
                "Community 2",
                true
        );

        return Arrays.asList(community1, community2);
    }
}