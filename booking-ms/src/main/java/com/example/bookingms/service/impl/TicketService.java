package com.example.bookingms.service.impl;

import com.example.bookingms.model.dto.FlightResponseDto;
import com.example.bookingms.model.dto.TicketResponseDto;
import com.example.bookingms.model.dto.request.TicketRequestDto;
import com.example.bookingms.model.entity.Ticket;
import com.example.bookingms.repository.TicketRepository;
import com.example.bookingms.service.FlightClient;
import com.example.bookingms.service.ITicketService;
import com.example.commonnotification.dto.request.KafkaRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final FlightClient flightClient;
    private final KafkaTemplate<String, KafkaRequest> kafkaTemplate;

    @Override
    public List<TicketResponseDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDto getTicketById(Long id) {
        return modelMapper.map(ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found!")), TicketResponseDto.class);
    }

    @Override
    public ResponseEntity<String> buyTicket(TicketRequestDto requestDto, Long flightId) {
        FlightResponseDto flightDto = flightClient.getFlightById(flightId);
        createAndSaveTicket(requestDto, flightDto);

        KafkaRequest kafkaRequest = createKafkaRequest(requestDto, flightDto);
        kafkaTemplate.send("ticket-topic",kafkaRequest);

        return ResponseEntity.ok().body("Ticket buy is successfully!");
    }

    private static KafkaRequest createKafkaRequest(TicketRequestDto requestDto, FlightResponseDto flightDto) {
        return KafkaRequest.builder()
                .email(requestDto.getEmail())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .fromAirlineId(flightDto.getFromAirlineId())
                .toAirlineId(flightDto.getToAirlineId())
                .departureDateTime(flightDto.getDepartureDateTime())
                .arrivalDateTime(flightDto.getArrivalDateTime())
                .price(flightDto.getInitialPrice())
                .build();
    }

    private void createAndSaveTicket(TicketRequestDto requestDto, FlightResponseDto flightDto) {
        Ticket ticket = Ticket.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .fromAirlineId(flightDto.getFromAirlineId())
                .toAirlineId(flightDto.getToAirlineId())
                .departureDateTime(flightDto.getDepartureDateTime())
                .arrivalDateTime(flightDto.getArrivalDateTime())
                .price(flightDto.getInitialPrice())
                .flightId(flightDto.getId())
                .buyDate(LocalDateTime.now())
                .build();
        ticketRepository.save(ticket);
    }

    public String getUsernameInToken(String token){
        return "Username";
    }

    @Override
    public ResponseEntity<String> downloadTicketPDF(Long ticketId) {
        return null;
    }
}
