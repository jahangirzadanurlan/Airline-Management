package com.example.bookingms.controller;

import com.example.bookingms.model.dto.request.TicketRequestDto;
import com.example.bookingms.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final ITicketService ticketService;

    @PostMapping("/tickets/{flightId}")
    public ResponseEntity<String> buyTicket(@PathVariable Long flightId, @RequestBody TicketRequestDto requestDto){
        return ticketService.buyTicket(requestDto,flightId);
    }

}
