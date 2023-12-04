package com.example.bookingms.service;

import com.example.bookingms.model.dto.response.PlaneResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "airplane-ms",url = "http://localhost:8083")
public interface AirplaneClient {

    @GetMapping("/airplane-ms/airplanes/{id}")
    PlaneResponseDto getAirplaneById(@PathVariable Long id);


}
