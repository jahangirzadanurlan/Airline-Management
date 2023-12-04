package com.example.commonms.service.impl;

import com.example.commonms.model.dto.request.AirlineRequestDto;
import com.example.commonms.model.dto.response.AirlineResponseDto;
import com.example.commonms.model.entity.Airline;
import com.example.commonms.repository.AirlineRepository;
import com.example.commonms.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirlineService implements IAirlineService {
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @Override
    public Airline getAirlineById(Long id) {
        Optional<Airline> airline = airlineRepository.findById(id);
        return airline.orElseThrow(() -> new RuntimeException("Airline not found!"));
    }

    @Override
    public List<Airline> searchAirline(AirlineRequestDto airlineRequestDto) {
        if (airlineRequestDto.getAirlineName() != null && !airlineRequestDto.getAirlineName().isEmpty()){
            return airlineRepository.findAllByAirlineNameContainingIgnoreCase(airlineRequestDto.getAirlineName());
        }else if (airlineRequestDto.getCountry() != null && !airlineRequestDto.getCountry().isEmpty()){
            return airlineRepository.findAllByCountryContainingIgnoreCase(airlineRequestDto.getCountry());
        }else {
            throw new RuntimeException("Airline Not found!");
        }
    }


}
