package com.github.ikarita.server.api.community;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenId;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.AutoConfigureSecurityAddons;
import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.security.SecurityConfiguration;
import com.github.ikarita.server.service.community.CommunityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = CommunityController.class, properties = {"com.c4-soft.springaddons.security.csrf-enabled=false"})
@AutoConfigureSecurityAddons
@Import(SecurityConfiguration.class)
class CommunityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CommunityService communityService;

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
    @OpenId(authorities = { "ANY" }, claims = @OpenIdClaims(preferredUsername = "Tonton Pirate"))
    void testGetCommunitiesWithAuthenticationIsOk() throws Exception {
        final CommunityDto community1 = new CommunityDto(1L, "Community 1", true);
        final CommunityDto community2 = new CommunityDto(2L, "Community 2", true);

        Mockito.when(communityService.getCommunities()).thenReturn(Arrays.asList(community1, community2));
        mockMvc.perform(get("/api/v1/communities"))
                .andExpect(status().isOk());
    }

    @Test
    @OpenId(authorities = { "WRONG" }, claims = @OpenIdClaims(preferredUsername = "Tonton Pirate"))
    void testPostCommunityWithWrongAuthorityIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityNotAuthenticatedIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @OpenId(authorities = { "COMMUNITY_CREATE" }, claims = @OpenIdClaims(preferredUsername = "Some User"))
    void testPostCommunityWithContributorIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        mockMvc.perform(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @OpenId("COMMUNITY_CREATE")
    void testPostCommunityWithAuthenticationIsOk() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);
        final CommunityDto communityDto = new CommunityDto(1L, "Community 1", true);

        Mockito.when(communityService.createCommunity(any())).thenReturn(communityDto);
        mockMvc.perform(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)))
                .andExpect(status().isCreated());
    }

    private byte[] toJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(dto);
    }
}