package com.example.flightms.service.impl;

import com.example.flightms.model.dto.request.FlightRequestDto;
import com.example.flightms.model.dto.response.FlightResponseDto;
import com.example.flightms.model.entity.Flight;
import com.example.flightms.repository.FlightRepository;
import com.example.flightms.service.IFlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightService implements IFlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> addFlight(FlightRequestDto requestDto) {
        Flight flight = modelMapper.map(requestDto, Flight.class);
        flightRepository.save(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body("Flight save is successfully!");
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        return flightRepository.findAll().stream()
                .peek(flight -> flight.setPrice(flight.getInitialPrice() + (flight.getInitialPrice() * (flight.getTicketIncreasePercentBYSeatsCount() + Flight.ticketIncreasePercentBYSaleTicketCount))/100))
                .map(flight -> modelMapper.map(flight,FlightResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FlightResponseDto getFlightById(Long id) {
        return modelMapper.map(flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found!")),FlightResponseDto.class);
    }

    @Override
    public ResponseEntity<String> updateFlight(Long id,FlightRequestDto requestDto) {
        Optional<Flight> flightById = flightRepository.findById(id);
        flightById.orElseThrow(() -> new RuntimeException("Flight not found!")).setAirplaneId(requestDto.getAirplaneId());
        flightById.get().setInitialPrice(requestDto.getInitialPrice());
        flightById.get().setFromAirlineId(requestDto.getFromAirlineId());
        flightById.get().setDepartureDateTime(requestDto.getDepartureDateTime());
        flightById.get().setToAirlineId(requestDto.getToAirlineId());
        flightById.get().setAirplaneId(requestDto.getAirplaneId());

        flightRepository.save(flightById.get());
        return ResponseEntity.ok().body("Updating is successfully!");
    }

    @Override
    public ResponseEntity<String> deleteFlight(Long id) {
        flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found!"));
        flightRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleting is successfully!");
    }

    @Override
    public ResponseEntity<String> patchFlight(Long id, String isFly) {
        Optional<Flight> flight = flightRepository.findById(id);

        if (isFly.equalsIgnoreCase("true") || isFly.equalsIgnoreCase("false")){
            flight.orElseThrow(() -> new RuntimeException("Plane not found!"))
                    .setFly(isFly.equalsIgnoreCase("true"));
            flightRepository.save(flight.get());
            return ResponseEntity.ok().body("Flight updated!");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body isFly => " + isFly + " is not true! ");
        }
    }
}
