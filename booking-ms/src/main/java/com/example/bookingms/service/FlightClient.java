package com.example.bookingms.service;

import com.example.bookingms.model.dto.response.FlightResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-ms",url = "http://localhost:8085")
public interface FlightClient {

    @GetMapping("/flight-ms/flights/{id}")
    FlightResponseDto getFlightById(@PathVariable Long id);

    @GetMapping("/flight-ms/flights/price/{id}")
    double getFlightPriceById(@PathVariable Long id);
}
