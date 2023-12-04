package com.example.commonms.controller;

import com.example.commonms.model.dto.request.AirlineRequestDto;
import com.example.commonms.model.entity.Airline;
import com.example.commonms.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonController {
    private final IAirlineService airlineService;

    @GetMapping("/airlines")
    public List<Airline> getAllAirlines(){
        return airlineService.getAllAirlines();
    }

    @GetMapping("/airlines/{id}")
    public Airline getAirlineById(@PathVariable Long id){
        return airlineService.getAirlineById(id);
    }

    @GetMapping("/airlines/search")
    public List<Airline> searchAirline(@RequestBody AirlineRequestDto airlineRequestDto){
        log.info(airlineRequestDto.toString());
        return airlineService.searchAirline(airlineRequestDto);
    }

}
