package com.github.ikarita.server;

import com.github.ikarita.server.api.community.CommunityController;
import com.github.ikarita.server.api.community.CommunityRoleController;
import com.github.ikarita.server.api.user.DataUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

class IkaritaServerApplicationTests extends AbstractIntegrationTest {
	@Autowired
	CommunityController communityController;
	@Autowired
	CommunityRoleController communityRoleController;
	@Autowired
    DataUserController dataUserController;

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
		assertNotNull(communityController);
		assertNotNull(communityRoleController);
		assertNotNull(dataUserController);
	}
}
