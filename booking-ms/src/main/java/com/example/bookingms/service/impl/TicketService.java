package com.example.bookingms.service.impl;

import com.example.bookingms.model.dto.response.FlightResponseDto;
import com.example.bookingms.model.dto.response.PlaneResponseDto;
import com.example.bookingms.model.dto.response.TicketResponseDto;
import com.example.bookingms.model.dto.request.TicketRequestDto;
import com.example.bookingms.model.entity.Ticket;
import com.example.bookingms.repository.TicketRepository;
import com.example.bookingms.service.AirplaneClient;
import com.example.bookingms.service.FlightClient;
import com.example.bookingms.service.ITicketService;
import com.example.commonfilegenerator.service.PdfGeneratorService;
import com.example.commonnotification.dto.request.KafkaRequest;
import com.example.commonsecurity.auth.SecurityHelper;
import com.example.commonsecurity.auth.services.JwtService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService implements ITicketService {
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final FlightClient flightClient;
    private final AirplaneClient airplaneClient;
    private final KafkaTemplate<String, KafkaRequest> kafkaTemplate;

    @Override
    public List<TicketResponseDto> getAllTickets(String authHeader) {
        String username = getUsernameInHeader(authHeader);

        return ticketRepository.findAllByUsername(username).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDto getTicketById(String authHeader,Long id) {
        String username = getUsernameInHeader(authHeader);

        return modelMapper.map(ticketRepository.findByIdAndUsername(id,username)
                .orElseThrow(() -> new RuntimeException("Ticket not found!")), TicketResponseDto.class);
    }

    @Override
    public ResponseEntity<String> buyTicket(String authHeader,TicketRequestDto requestDto, Long flightId) {
        String username = getUsernameInHeader(authHeader);
        double flightPrice = getFlightPrice(flightId); //exception
        FlightResponseDto flightDto = flightClient.getFlightById(flightId);

        log.info("flightDto.getAirplaneId() => {}",flightDto.getAirplaneId());
        PlaneResponseDto airplane = airplaneClient.getAirplaneById(flightDto.getAirplaneId());
        if (requestDto.getPlaneSeatNumber() > airplane.getMasSeats()){
            throw new RuntimeException("There is no such seat number");
        }

        if (ticketRepository.findByPlaneSeatNumber(requestDto.getPlaneSeatNumber()).isPresent()){
            throw new RuntimeException("This seat already taken");
        }

        createAndSaveTicket(username,requestDto, flightDto,flightPrice);

        KafkaRequest kafkaRequest = createKafkaRequest(requestDto, flightDto,flightPrice);
        kafkaTemplate.send("ticket-topic",kafkaRequest);

        return ResponseEntity.ok().body("Ticket buy is successfully!");
    }

    public double getFlightPrice(Long flightId){
        return flightClient.getFlightPriceById(flightId);
    }

    private static KafkaRequest createKafkaRequest(TicketRequestDto requestDto, FlightResponseDto flightDto,double flightPrice) {
        return KafkaRequest.builder()
                .email(requestDto.getEmail())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .fromAirlineId(flightDto.getFromAirlineId())
                .toAirlineId(flightDto.getToAirlineId())
                .departureDateTime(flightDto.getDepartureDateTime())
                .arrivalDateTime(flightDto.getArrivalDateTime())
                .price(flightPrice)
                .build();
    }

    private void createAndSaveTicket(String username,TicketRequestDto requestDto, FlightResponseDto flightDto,double flightPrice) {
        Ticket ticket = Ticket.builder()
                .username(username)
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .fromAirlineId(flightDto.getFromAirlineId())
                .toAirlineId(flightDto.getToAirlineId())
                .departureDateTime(flightDto.getDepartureDateTime())
                .arrivalDateTime(flightDto.getArrivalDateTime())
                .price(flightPrice)
                .flightId(flightDto.getId())
                .planeSeatNumber(requestDto.getPlaneSeatNumber())
                .buyDate(LocalDateTime.now())
                .build();
        Ticket.saleTicketCount += 1;
        ticketRepository.save(ticket);
    }

    public String getUsernameInHeader(String authHeader){
        if (!securityHelper.authHeaderIsValid(authHeader)){
            throw new RuntimeException("Authorization header not true!");
        }
        String jwtToken=authHeader.substring(7);

        return jwtService.extractUsername(jwtToken);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadTicketPDF(String authHeader, Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        String username = getUsernameInHeader(authHeader);
        if (!username.equals(ticket.orElseThrow(() -> new RuntimeException("Ticket not found!")).getUsername())){
            throw new RuntimeException("Unauthenticated request!");
        }

        String content = ticket.get().getFirstName() + " " + ticket.get().getLastName() + " your fromAirlineId=>" +
                ticket.get().getFromAirlineId() + " toAirlineId=> " + ticket.get().getToAirlineId() + " ticket buying is successfully. \n" + "Departure Date Time => " + ticket.get().getDepartureDateTime()
                + "\nArrival Date Time => " + ticket.get().getArrivalDateTime() + "\nTicket Price => " + ticket.get().getPrice();

        // Kullanıcının bilgilerini içeren PDF dosyasını oluşturun
        byte[] pdfContent = PdfGeneratorService.generatePdf(content);

        // Dosyanın boyutunu alın
        long contentLength;
        if (pdfContent != null) {
            contentLength = pdfContent.length;
        }else {
            throw new RuntimeException("Pdf content is null!");
        }

        // Dosyanın içeriğini temsil eden bir InputStreamResource oluşturun
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfContent));

        // Dosya adını belirtin
        String fileName = "ticket_details.pdf";

        // Response Headers ayarlayın
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(contentLength);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);

        // ResponseEntity'yi oluşturun
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }
}
