package com.github.ikarita.server.api;

import com.github.ikarita.server.api.community.CommunityController;
import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.service.community.CommunityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CommunityController.class)
class CommunityControllerTest extends AbstractControllerTest {
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
    void testGetCommunitiesWithAuthenticationIsOk() throws Exception {
        final CommunityDto community1 = new CommunityDto(1L, "Community 1", true);
        final CommunityDto community2 = new CommunityDto(2L, "Community 2", true);

        Mockito.when(communityService.getCommunities()).thenReturn(Arrays.asList(community1, community2));
        performAuthenticated(get("/api/v1/communities"), admin())
                .andExpect(status().isOk());
    }

    @Test
    void testPostCommunityWithViewerIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), viewer())
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityWithModeratorIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), moderator())
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityWithContributorIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), contributor())
                .andExpect(status().isCreated());
    }

    @Test
    void testPostCommunityWithAuthenticationIsOk() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);
        final CommunityDto communityDto = new CommunityDto(1L, "Community 1", true);

        Mockito.when(communityService.createCommunity(any())).thenReturn(communityDto);
        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), admin())
                .andExpect(status().isCreated());
    }
}