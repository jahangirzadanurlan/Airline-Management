package com.example.bookingms.service;

import com.example.bookingms.model.dto.response.TicketResponseDto;
import com.example.bookingms.model.dto.request.TicketRequestDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ITicketService {
    List<TicketResponseDto> getAllTickets(String authHeader);
    TicketResponseDto getTicketById(String authHeader,Long id);
    ResponseEntity<String> buyTicket(String authHeader,TicketRequestDto requestDto,Long flightId);
    ResponseEntity<InputStreamResource> downloadTicketPDF(String authHeader,Long ticketId) throws IOException;
}
