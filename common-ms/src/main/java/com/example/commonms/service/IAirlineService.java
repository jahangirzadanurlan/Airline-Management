package com.example.commonms.service;

import com.example.commonms.model.dto.request.AirlineRequestDto;
import com.example.commonms.model.entity.Airline;

import java.util.List;

public interface IAirlineService {
    List<Airline> getAllAirlines();
    Airline getAirlineById(Long id);
    List<Airline> searchAirline(AirlineRequestDto airlineRequestDto);

}
