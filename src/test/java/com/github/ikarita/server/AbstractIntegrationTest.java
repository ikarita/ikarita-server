package com.github.ikarita.server;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIntegrationTest.DataSourceInitializer.class)
@Testcontainers
public abstract class AbstractIntegrationTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.2");
    public static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.2");

    static {
        postgres.start();
        keycloak.start();
    }

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.security.oauth2.resourceserver.jwt.issuer-uri=" + keycloak.getAuthServerUrl()
            );
        }
    }
}
