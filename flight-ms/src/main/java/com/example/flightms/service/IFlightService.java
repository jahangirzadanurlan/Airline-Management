package com.example.flightms.service;

import com.example.flightms.model.dto.request.FlightRequestDto;
import com.example.flightms.model.dto.response.FlightResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFlightService {
    ResponseEntity<String> addFlight(FlightRequestDto requestDto);
    List<FlightResponseDto> getAllFlights();
    FlightResponseDto getFlightById(Long id);
    ResponseEntity<String> updateFlight(Long id,FlightRequestDto requestDto);
    ResponseEntity<String> deleteFlight(Long id);
    ResponseEntity<String> patchFlight(Long id,String isFly);
}
