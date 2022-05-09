package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.service.CommunityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommunityController.class)
class CommunityControllerTest {
    @MockBean
    CommunityService communityService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetCommunitiesWithoutAuthenticationIsForbidden() throws Exception {
        Mockito.when(communityService.getCommunities()).thenReturn(communityDtoList());
        mockMvc.perform(get("/api/v1/communities"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetCommunityWithoutAuthenticationIsForbidden() throws Exception {
        Mockito.when(communityService.getCommunities()).thenReturn(communityDtoList());
        mockMvc.perform(get("/api/v1/communities/1"))
                .andExpect(status().isForbidden());
    }

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