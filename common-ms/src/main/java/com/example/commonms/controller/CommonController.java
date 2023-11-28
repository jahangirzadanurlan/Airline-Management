package com.example.commonms.controller;

import com.example.commonms.model.dto.request.AirlineRequestDto;
import com.example.commonms.model.dto.response.AirlineResponseDto;
import com.example.commonms.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final IAirlineService airlineService;

    @GetMapping
    public List<AirlineResponseDto> getAllAirlines(){
        return airlineService.getAllAirlines();
    }

    @GetMapping("/{id}")
    public AirlineResponseDto getAirlineById(@PathVariable Long id){
        return airlineService.getAirlineById(id);
    }

}
