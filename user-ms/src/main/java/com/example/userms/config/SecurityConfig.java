package com.example.userms.config;

import com.example.commonsecurity.config.ApplicationSecurityConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@Configuration
@EnableWebSecurity
@ComponentScan("com.example.commonsecurity")
public class SecurityConfig implements ApplicationSecurityConfigurer {
    private static final String POST_USER_REGISTER = "/user/registration";

    @Override
    public void configure(HttpSecurity http) throws Exception {
                log.info("Configuring role based access control user management");
                http.authorizeRequests()
                        .antMatchers(HttpMethod.POST,POST_USER_REGISTER).permitAll();
    }
}
