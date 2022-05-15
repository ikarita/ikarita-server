package com.github.ikarita.server;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Ikarita Server API", version = "1"),
		servers = {@Server(url = "http://localhost:8080")},
		tags = {
				@Tag(name="Communities", description = "Access and management for communities."),
				@Tag(name="Users", description = "Access and management for users."),
				@Tag(name="Tokens", description = "Tokens generation.")
		}
)
@SecurityScheme(name = "Bearer JWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class IkaritaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(IkaritaServerApplication.class, args);
	}
}
