package com.github.ikarita.server.security;

import com.c4_soft.springaddons.security.oauth2.spring.C4MethodSecurityExpressionHandler;
import com.c4_soft.springaddons.security.oauth2.spring.C4MethodSecurityExpressionRoot;
import com.github.ikarita.server.model.entities.community.Community;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Converter<Jwt, ? extends AbstractAuthenticationToken> authenticationConverter, ServerProperties serverProperties)
            throws Exception {

        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(authenticationConverter);
        http.csrf().disable();
        http.anonymous();
        http.cors().configurationSource(corsConfigurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled()) {
            http.requiresChannel().anyRequest().requiresSecure();
        } else {
            http.requiresChannel().anyRequest().requiresInsecure();
        }

        http.authorizeRequests()
                .antMatchers("/actuator/health/readiness", "/actuator/health/liveness", "/v3/api-docs/**")
                .permitAll();

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api.v1/**", configuration);

        return source;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new C4MethodSecurityExpressionHandler(CommunityMethodSecurityExpressionRoot::new);
    }

    static final class CommunityMethodSecurityExpressionRoot extends C4MethodSecurityExpressionRoot {
        public boolean hasCommunityAuthority(String community, String authority){
            return false;
        }
    }
}
