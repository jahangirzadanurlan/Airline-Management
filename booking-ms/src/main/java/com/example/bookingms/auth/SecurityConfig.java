package com.example.bookingms.auth;

import com.example.commonsecurity.config.ApplicationSecurityConfigurer;
import com.example.commonsecurity.model.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@ComponentScan("com.example.commonsecurity")
public class SecurityConfig implements ApplicationSecurityConfigurer {
    private final JwtRequestFilter jwtAuthenticationFilter;

    private static final String POST_GET_TICKET = "/tickets/**";

    @Override
    public void configure(HttpSecurity http) throws Exception {
                log.info("Configuring role based access control user management");

                http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(HttpMethod.GET,"/sale-ticket-count").permitAll()
                                .antMatchers(HttpMethod.POST,"/sale-ticket-count").permitAll()
                            .antMatchers(POST_GET_TICKET).hasRole(RoleType.USER.name())
                                .antMatchers())

                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

