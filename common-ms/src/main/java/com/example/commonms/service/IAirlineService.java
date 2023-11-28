package com.example.commonms.service;

import com.example.commonms.model.dto.response.AirlineResponseDto;

import java.util.List;

public interface IAirlineService {
    List<AirlineResponseDto> getAllAirlines();
    AirlineResponseDto getAirlineById(Long id);

}
