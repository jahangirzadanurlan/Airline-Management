package com.example.bookingms.service.impl;

import com.example.bookingms.model.dto.response.FlightResponseDto;
import com.example.bookingms.model.dto.response.TicketResponseDto;
import com.example.bookingms.model.dto.request.TicketRequestDto;
import com.example.bookingms.model.entity.Ticket;
import com.example.bookingms.repository.TicketRepository;
import com.example.bookingms.service.FlightClient;
import com.example.bookingms.service.ITicketService;
import com.example.commonnotification.dto.request.KafkaRequest;
import com.example.commonsecurity.auth.SecurityHelper;
import com.example.commonsecurity.auth.services.JwtService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final FlightClient flightClient;
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
        FlightResponseDto flightDto = flightClient.getFlightById(flightId);

        createAndSaveTicket(username,requestDto, flightDto);

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

    private void createAndSaveTicket(String username,TicketRequestDto requestDto, FlightResponseDto flightDto) {
        Ticket ticket = Ticket.builder()
                .username(username)
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

    public String getUsernameInHeader(String authHeader){
        if (!securityHelper.authHeaderIsValid(authHeader)){
            throw new RuntimeException("Authorization header not true!");
        }
        String jwtToken=authHeader.substring(7);

        return jwtService.extractUsername(jwtToken);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadTicketPDF(String authHeader, Long ticketId) throws IOException {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        String username = getUsernameInHeader(authHeader);
        if (!username.equals(ticket.orElseThrow(() -> new RuntimeException("Ticket not found!")).getUsername())){
            throw new RuntimeException("Unauthenticated request!");
        }

        // Kullanıcının bilgilerini içeren PDF dosyasını oluşturun
        byte[] pdfContent = createUserDetailsPDF(ticket.get());

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

    private byte[] createUserDetailsPDF(Ticket ticket) throws IOException {
        // PDF oluştur
        Document document = new Document();

        String content = ticket.getFirstName() + " " + ticket.getLastName() + " your fromAirlineId=>" +
                ticket.getFromAirlineId() + " toAirlineId=> " + ticket.getToAirlineId() + " ticket buying is successfully. \n" + "Departure Date Time => " + ticket.getDepartureDateTime()
                + "\nArrival Date Time => " + ticket.getArrivalDateTime() + "\nTicket Price => " + ticket.getPrice();


        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();
            // Başlık
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE);
            Paragraph title = new Paragraph("Ticket Information", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Boşluk
            document.add(Chunk.NEWLINE);

            // Metin
            Font contentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            Paragraph para = new Paragraph(content, contentFont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para);

            document.add(new Paragraph(content));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
