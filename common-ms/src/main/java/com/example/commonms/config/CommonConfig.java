package com.example.commonms.config;

import com.example.commonms.model.entity.Airline;
import com.example.commonms.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {
    private final AirlineRepository airlineRepository;

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @PostConstruct
    public void createAndSaveAirlines(){
        if(airlineRepository.findAll().isEmpty()){
            List<Airline> airlines = Arrays.asList(

                    Airline.builder()
                            .country("Kenya")
                            .airlineName("Jomo Kenyatta International Airport")
                            .build(),

                    Airline.builder()
                            .country("South Korea")
                            .airlineName("Incheon International Airport")
                            .build(),

                    Airline.builder()
                            .country("Germany")
                            .airlineName("Frankfurt Airport")
                            .build(),

                    Airline.builder()
                            .country("Australia")
                            .airlineName("Sydney Kings ford Smith Airport")
                            .build(),

                    Airline.builder()
                            .country("Singapore")
                            .airlineName("Changi Airport")
                            .build(),

                    Airline.builder()
                            .country("Holland")
                            .airlineName("Amsterdam Airport Schiphol")
                            .build(),

                    Airline.builder()
                            .country("Hong Kong")
                            .airlineName("Hong Kong International Airport")
                            .build(),

                    Airline.builder()
                            .country("Qatar")
                            .airlineName("Hamad International Airport")
                            .build(),

                    Airline.builder()
                            .country("Thailand")
                            .airlineName("Suvarnabhumi Airport")
                            .build(),

                    Airline.builder()
                            .country("Canada")
                            .airlineName("Toronto Pearson International Airport")
                            .build()
            );

            airlineRepository.saveAll(airlines);
        }
    }
}
