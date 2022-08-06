package com.github.ikarita.server.api.user;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenId;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.AutoConfigureSecurityAddons;
import com.github.ikarita.server.security.SecurityConfiguration;
import com.github.ikarita.server.service.user.DataUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataUserController.class)
@AutoConfigureSecurityAddons
@Import(SecurityConfiguration.class)
class DataUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DataUserService dataUserService;

    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/communities/roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    @OpenId(authorities = { "WRONG" }, claims = @OpenIdClaims(preferredUsername = "Tonton Pirate"))
    void testGetCommunityWithWrongAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/communities/roles"))
                .andExpect(status().isForbidden());
    }
}
