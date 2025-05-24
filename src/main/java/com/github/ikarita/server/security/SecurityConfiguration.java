package com.github.ikarita.server.security;

import com.github.ikarita.server.service.user.UserSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            ServerProperties serverProperties,
            @Value("${origins:[]}") String[] origins,
            @Value("${permit-all:[]}") String[] permitAll)
            throws Exception {

        //http.oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));

        // Enable and configure CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource(origins)));

        // State-less session (state in access-token only)
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disable CSRF because of state-less session-management
        http.csrf(AbstractHttpConfigurer::disable);

        // Return 401 (unauthorized) instead of 302 (redirect to login) when authorization is missing or invalid
        http.exceptionHandling(eh -> eh.authenticationEntryPoint((request, response, authException) -> {
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Restricted Content\"");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }));

        // If SSL enabled, disable http (https only)
        if (serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled()) {
            http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        }

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(Stream.of(permitAll).map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new)).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource(String[] origins) {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(origins));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(UserSecurityService userSecurityService) {
        return new IkaritaSecurityExpressionHandler(CommunityMethodSecurityExpressionRoot::new, userSecurityService);
    }
}