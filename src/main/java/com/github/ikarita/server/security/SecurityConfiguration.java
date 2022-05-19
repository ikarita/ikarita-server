package com.github.ikarita.server.security;

import com.github.ikarita.server.security.filter.AuthenticationFilter;
import com.github.ikarita.server.security.filter.AuthorizationFilter;
import com.github.ikarita.server.security.jwt.JwtGenerator;
import com.github.ikarita.server.security.jwt.JwtValidator;
import com.github.ikarita.server.security.permissions.AllowedPaths;
import com.github.ikarita.server.service.user.LocalUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtValidator jwtValidator;
    private final JwtGenerator jwtGenerator;

    private final LocalUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), jwtGenerator);
        authenticationFilter.setFilterProcessesUrl("/api/v1/token/access");
        final AuthorizationFilter authorizationFilter = new AuthorizationFilter(jwtValidator);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeHttpRequests().antMatchers(AllowedPaths.swagger()).permitAll();
        http.authorizeHttpRequests().antMatchers(AllowedPaths.all()).permitAll();
        http.authorizeHttpRequests().antMatchers(POST, AllowedPaths.post()).permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
