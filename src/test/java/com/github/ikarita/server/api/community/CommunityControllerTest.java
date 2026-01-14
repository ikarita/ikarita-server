package com.github.ikarita.server.api.community;

import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.repository.community.CommunityRepository;
import com.github.ikarita.server.repository.user.CommunityUserRepository;
import com.github.ikarita.server.repository.user.UserRepository;
import com.github.ikarita.server.security.SecurityConfiguration;
import com.github.ikarita.server.service.community.CommunityService;
import com.github.ikarita.server.service.user.UserSecurityServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityController.class)
@Import({SecurityConfiguration.class, UserSecurityServiceImpl.class})
class CommunityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private CommunityService communityService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private CommunityRepository communityRepository;
    @MockitoBean
    private CommunityUserRepository communityUserRepository;

    @Test
    void testGetCommunitiesWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities")).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = { "ANY" })
    void testGetCommunitiesWithAuthenticationIsOk() throws Exception {
        final CommunityDto community1 = new CommunityDto(1L, "Community 1", true);
        final CommunityDto community2 = new CommunityDto(2L, "Community 2", true);

        Mockito.when(communityService.getCommunities()).thenReturn(Arrays.asList(community1, community2));
        mockMvc.perform(get("/api/v1/communities").with(oidcLogin()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = { "WRONG" })
    void testPostCommunityWithWrongAuthorityIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(post("/api/v1/communities")
                        .with(oidcLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newCommunityDto))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityNotAuthenticatedIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = { "COMMUNITY_CREATE" })
    void testPostCommunityDeactivate() throws Exception {
        mockMvc.perform(post("/api/v1/communities/deactivate/1").with(oidcLogin()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityWithContributorIsOk() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(
                post("/api/v1/communities")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_COMMUNITY_CREATE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newCommunityDto)))
                .andExpect(status().isCreated()
                );
    }

    @Test
    void testPostCommunityWithAuthenticationIsOk() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);
        final CommunityDto communityDto = new CommunityDto(1L, "Community 1", true);

        Mockito.when(communityService.createCommunity(any())).thenReturn(communityDto);
        mockMvc.perform(post("/api/v1/communities")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_COMMUNITY_CREATE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newCommunityDto))
                )
                .andExpect(status().isCreated());
    }

    private byte[] toJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(dto);
    }
}