package com.example.flightms.controller;

import com.example.flightms.model.dto.request.FlightRequestDto;
import com.example.flightms.model.dto.response.FlightResponseDto;
import com.example.flightms.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightController {
    private final IFlightService flightService;

    @PostMapping("/flights")
    public ResponseEntity<String> addFlight(FlightRequestDto requestDto){
        return flightService.addFlight(requestDto);
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Long id,@RequestBody FlightRequestDto requestDto){
        return flightService.updateFlight(id,requestDto);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id){
        return flightService.deleteFlight(id);
    }

    @PatchMapping("/flights/{id}")
    public ResponseEntity<String> patchFlight(@PathVariable Long id, @RequestBody String isFly){
        return flightService.patchFlight(id,isFly);
    }

    @GetMapping("/flights")
    public List<FlightResponseDto> getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/flights/{id}")
    public FlightResponseDto getFlightById(@PathVariable Long id){
        return flightService.getFlightById(id);
    }

}
