package com.example.bookingms.service;

import com.example.bookingms.model.dto.TicketResponseDto;
import com.example.bookingms.model.dto.request.TicketRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITicketService {
    List<TicketResponseDto> getAllTickets();
    TicketResponseDto getTicketById(Long id);
    ResponseEntity<String> buyTicket(TicketRequestDto requestDto,Long flightId);
    ResponseEntity<String> downloadTicketPDF(Long ticketId);
}
