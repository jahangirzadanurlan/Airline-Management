package com.example.userms.config;

import com.example.commonsecurity.config.ApplicationSecurityConfigurer;
import com.example.commonsecurity.model.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@ComponentScan("com.example.commonsecurity")
public class SecurityConfig implements ApplicationSecurityConfigurer {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String POST_GET_USER = "/auth/**";
    private static final String POST_ADMIN_REGISTER = "/admin/**";

    @Override
    public void configure(HttpSecurity http) throws Exception {
                log.info("Configuring role based access control user management");

                http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                            .antMatchers(POST_GET_USER).permitAll()
                            .antMatchers(POST_ADMIN_REGISTER).hasRole(RoleType.ADMIN.name()))
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

