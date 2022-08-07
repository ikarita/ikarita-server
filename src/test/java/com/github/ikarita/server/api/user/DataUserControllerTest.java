package com.github.ikarita.server.api.user;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenId;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.AutoConfigureSecurityAddons;
import com.github.ikarita.server.api.community.CommunityRoleController;
import com.github.ikarita.server.model.dto.community.CommunitySimpleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityRoleDto;
import com.github.ikarita.server.security.SecurityConfiguration;
import com.github.ikarita.server.service.community.CommunityRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityRoleController.class)
@AutoConfigureSecurityAddons
@Import(SecurityConfiguration.class)
class DataUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CommunityRoleService communityRoleService;


    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/community/roles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @OpenId(authorities = { "WRONG" }, claims = @OpenIdClaims(preferredUsername = "Tonton Pirate"))
    void testGetCommunityWithWrongAuthenticationIsUnauthorized() throws Exception {
        CommunitySimpleDto communitySimpleDto = new CommunitySimpleDto(1L, "Cool Kids");
        NewCommunityRoleDto roleDto = new NewCommunityRoleDto("exploited", communitySimpleDto);
        mockMvc.perform(post("/api/v1/community/roles").contentType(MediaType.APPLICATION_JSON).content(toJson(communitySimpleDto)))
                .andExpect(status().isForbidden());
    }

    private byte[] toJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(dto);
    }
}
