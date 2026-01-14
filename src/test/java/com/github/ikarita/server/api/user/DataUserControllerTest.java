package com.github.ikarita.server.api.user;

import com.github.ikarita.server.api.community.CommunityRoleController;
import com.github.ikarita.server.model.dto.community.CommunitySimpleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityRoleDto;
import com.github.ikarita.server.security.SecurityConfiguration;
import com.github.ikarita.server.service.community.CommunityRoleService;
import com.github.ikarita.server.service.user.UserSecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityRoleController.class)
@Import(SecurityConfiguration.class)
class DataUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CommunityRoleService communityRoleService;
    @MockitoBean
    private UserSecurityService userSecurityService;


    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/community/roles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = { "WRONG" })
    void testGetCommunityWithWrongAuthenticationIsUnauthorized() throws Exception {
        final CommunitySimpleDto communitySimpleDto = new CommunitySimpleDto(1L, "Cool Kids");
        mockMvc.perform(post("/api/v1/community/roles").contentType(MediaType.APPLICATION_JSON).content(toJson(communitySimpleDto)))
                .andExpect(status().isUnauthorized());
    }

    private byte[] toJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(dto);
    }
}
