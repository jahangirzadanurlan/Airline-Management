package com.example.flightms.service.impl;

import com.example.flightms.model.dto.response.FlightResponseDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Scheduled(fixedRate = 10000)
    public void changeTicketPrice(){
        FlightResponseDto.initialPrice = 100;
    }
}
