package com.github.ikarita.server;

import com.github.ikarita.server.model.dto.NewCommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleForUserDto;
import com.github.ikarita.server.model.dto.NewUserDto;
import com.github.ikarita.server.security.UserRole;
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
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saverRole(new NewCommunityRoleDto("ROLE_USER"));
			userService.saverRole(new NewCommunityRoleDto("ROLE_MANAGER"));
			userService.saverRole(new NewCommunityRoleDto("ROLE_ADMIN"));
			userService.saverRole(new NewCommunityRoleDto("ROLE_SUPER_ADMIN"));

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
