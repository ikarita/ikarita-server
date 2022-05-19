package com.github.ikarita.server;

import com.github.ikarita.server.api.community.CommunityController;
import com.github.ikarita.server.api.community.CommunityRoleController;
import com.github.ikarita.server.api.user.TokenController;
import com.github.ikarita.server.api.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class IkaritaServerApplicationTests extends AbstractIntegrationTest {
	@Autowired
	CommunityController communityController;
	@Autowired
	CommunityRoleController communityRoleController;
	@Autowired
	TokenController tokenController;
	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
		assertNotNull(communityController);
		assertNotNull(communityRoleController);
		assertNotNull(tokenController);
		assertNotNull(userController);
	}
}
