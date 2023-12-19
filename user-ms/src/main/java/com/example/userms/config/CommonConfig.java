package com.example.userms.config;

import com.example.commonsecurity.model.RoleType;
import com.example.userms.model.entity.Role;
import com.example.userms.repository.RoleRepository;
import com.example.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CommonConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findUserByUsernameOrEmail(username)
                .orElseThrow(() -> new RuntimeException("Username not found: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public CommandLineRunner saveRoles() {
        return args -> {
            Role userRole = Role.builder()
                    .name(RoleType.USER)
                    .build();

            Role adminRole = Role.builder()
                    .name(RoleType.ADMIN)
                    .build();

            log.info("Roles saved to database");
            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        };
    }


}
