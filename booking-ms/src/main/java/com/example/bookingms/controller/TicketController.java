package com.example.bookingms.controller;

import com.example.bookingms.model.dto.request.TicketRequestDto;
import com.example.bookingms.model.dto.response.TicketResponseDto;
import com.example.bookingms.model.entity.Ticket;
import com.example.bookingms.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final ITicketService ticketService;

    @PostMapping("/tickets/{flightId}")
    public ResponseEntity<String> buyTicket(@RequestHeader(name = "Authorization") String authHeader,@PathVariable Long flightId, @RequestBody TicketRequestDto requestDto){
        return ticketService.buyTicket(authHeader,requestDto,flightId);
    }

    @GetMapping("/tickets")
    public List<TicketResponseDto> userAllTickets(@RequestHeader(name = "Authorization") String authHeader){
        return ticketService.getAllTickets(authHeader);
    }

    @GetMapping("/tickets/{ticketId}")
    public TicketResponseDto userTicketByTicketId(@RequestHeader(name = "Authorization") String authHeader, @PathVariable Long ticketId){
        return ticketService.getTicketById(authHeader,ticketId);
    }

    @GetMapping("/tickets/{ticketId}/pdf")
    public ResponseEntity<InputStreamResource> downloadTicketDetails(@RequestHeader(name = "Authorization") String authHeader,@PathVariable Long ticketId) throws IOException {
        return ticketService.downloadTicketPDF(authHeader,ticketId);
    }

    @GetMapping("/sale-ticket-count")
    public int getSaleTicketCount(){
        return Ticket.saleTicketCount;
    }

    @PostMapping("/sale-ticket-count")
    public void changeSaleTicketCount(){
        Ticket.saleTicketCount = 0;
    }
}

