package com.github.ikarita.server;

import com.github.ikarita.server.model.dto.NewRoleDto;
import com.github.ikarita.server.model.dto.NewRoleForUserDto;
import com.github.ikarita.server.model.dto.NewUserDto;
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
			userService.saverRole(new NewRoleDto("ROLE_USER"));
			userService.saverRole(new NewRoleDto("ROLE_MANAGER"));
			userService.saverRole(new NewRoleDto("ROLE_ADMIN"));
			userService.saverRole(new NewRoleDto("ROLE_SUPER_ADMIN"));

			userService.saveUser(new NewUserDto("John Travolta", "john@gmail.com", "1234"));
			userService.saveUser(new NewUserDto("Jim Carray", "jim@gmail.com", "1234"));
			userService.saveUser(new NewUserDto("Will Smith", "will@gmail.com", "1234"));

			userService.addRoleToUser(new NewRoleForUserDto("John Travolta", "ROLE_USER"));
			userService.addRoleToUser(new NewRoleForUserDto("John Travolta", "ROLE_MANAGER"));

			userService.addRoleToUser(new NewRoleForUserDto("Jim Carray", "ROLE_USER"));

			userService.addRoleToUser(new NewRoleForUserDto("Will Smith", "ROLE_USER"));
			userService.addRoleToUser(new NewRoleForUserDto("Will Smith", "ROLE_ADMIN"));
			userService.addRoleToUser(new NewRoleForUserDto("Will Smith", "ROLE_SUPER_ADMIN"));
		};
	}
}
