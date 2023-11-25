package com.example.bookingms.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
