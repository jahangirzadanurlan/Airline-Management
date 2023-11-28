package com.example.commonms.service.impl;

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
    public List<AirlineResponseDto> getAllAirlines() {
        return airlineRepository.findAll().stream()
                .map(airline -> modelMapper.map(airline, AirlineResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AirlineResponseDto getAirlineById(Long id) {
        Optional<Airline> airline = airlineRepository.findById(id);
        return modelMapper.map(airline.orElseThrow(() -> new RuntimeException("Airline not found!")),AirlineResponseDto.class);
    }
}
