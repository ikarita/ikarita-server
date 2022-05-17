package com.github.ikarita.server;

import com.github.ikarita.server.api.CommunityController;
import com.github.ikarita.server.api.CommunityRoleController;
import com.github.ikarita.server.api.TokenController;
import com.github.ikarita.server.api.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IkaritaServerApplicationTests {

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
