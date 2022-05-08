package com.github.ikarita.server;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.model.mappers.CommunityMapper;
import com.github.ikarita.server.security.UserRole;
import com.github.ikarita.server.service.CommunityRoleService;
import com.github.ikarita.server.service.CommunityService;
import com.github.ikarita.server.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IkaritaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IkaritaServerApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, CommunityRoleService communityRoleService, CommunityService communityService, CommunityMapper communityMapper){
		return args -> {
			CommunityDto niceCommunityDto = communityService.createCommunity(new NewCommunityDto("Nice community", true));
			CommunitySimpleDto niceCommunity = communityMapper.asSimpleDto(communityMapper.asEntity(niceCommunityDto));
			communityRoleService.createRole(new NewCommunityRoleDto("ROLE_USER", niceCommunity));
			communityRoleService.createRole(new NewCommunityRoleDto("ROLE_MANAGER", niceCommunity));
			communityRoleService.createRole(new NewCommunityRoleDto("ROLE_ADMIN", niceCommunity));
			communityRoleService.createRole(new NewCommunityRoleDto("ROLE_SUPER_ADMIN", niceCommunity));

			userService.saveUser(new NewUserDto("John Travolta", "john@gmail.com", "1234"));
			userService.saveUser(new NewUserDto("Jim Carray", "jim@gmail.com", "1234"));
			userService.saveUser(new NewUserDto("Will Smith", "will@gmail.com", "1234"));

			userService.addRoleToUser(new NewCommunityRoleForUserDto("John Travolta", UserRole.VIEWER.name()));
			userService.addRoleToUser(new NewCommunityRoleForUserDto("John Travolta", UserRole.MODERATOR.name()));

			userService.addRoleToUser(new NewCommunityRoleForUserDto("Jim Carray", UserRole.CONTRIBUTOR.name()));

			userService.addRoleToUser(new NewCommunityRoleForUserDto("Will Smith", UserRole.ADMIN.name()));
			userService.addRoleToUser(new NewCommunityRoleForUserDto("Will Smith", UserRole.MODERATOR.name()));
			userService.addRoleToUser(new NewCommunityRoleForUserDto("Will Smith", UserRole.CONTRIBUTOR.name()));
		};
	}
}
