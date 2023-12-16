package com.example.flightms.service.impl;

import com.example.commonexception.enums.ExceptionsEnum;
import com.example.commonexception.exceptions.GeneralException;
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
        log.info(requestDto.toString());
        Flight flight = modelMapper.map(requestDto, Flight.class);
        log.info(flight.toString());
        flightRepository.save(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body("Flight save is successfully!");
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        return flightRepository.findAll().stream()
                .peek(flight -> {
                    double flightPrice = getFlightPrice(flight.getId());
                    flight.setPrice(flightPrice);
                    log.info("flight initial-price => {}",flight.getInitialPrice());
                    log.info("flight  Ticket Increase Percent BY Seats Count => {}",flight.getTicketIncreasePercentBYSeatsCount());
                    log.info("flight  Ticket Increase Percent BY Sale Ticket Count => {}",Flight.ticketIncreasePercentBYSaleTicketCount);
                    log.info("flight  common price => {}", flightPrice);

                })
                .map(flight -> modelMapper.map(flight,FlightResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public double getFlightPrice(Long flightId) {
        Optional<Flight> flight = flightRepository.findById(flightId);
        flight.orElseThrow(() -> new GeneralException(ExceptionsEnum.FLIGHT_NOT_FOUND));
        return flight.get().getInitialPrice() + (flight.get().getInitialPrice() * (flight.get().getTicketIncreasePercentBYSeatsCount() + Flight.ticketIncreasePercentBYSaleTicketCount)) / 100;
    }

    @Override
    public FlightResponseDto getFlightById(Long id) {
        return modelMapper.map(flightRepository.findById(id).orElseThrow(() -> new GeneralException(ExceptionsEnum.FLIGHT_NOT_FOUND)),FlightResponseDto.class);
    }

    @Override
    public ResponseEntity<String> updateFlight(Long id,FlightRequestDto requestDto) {
        Optional<Flight> flightById = flightRepository.findById(id);
        flightById.orElseThrow(() -> new GeneralException(ExceptionsEnum.FLIGHT_NOT_FOUND)).setAirplaneId(requestDto.getAirplaneId());
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
        flightRepository.findById(id).orElseThrow(() -> new GeneralException(ExceptionsEnum.FLIGHT_NOT_FOUND));
        flightRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleting is successfully!");
    }

    @Override
    public ResponseEntity<String> patchFlight(Long id, String isFly) {
        Optional<Flight> flight = flightRepository.findById(id);

        if (isFly.equalsIgnoreCase("true") || isFly.equalsIgnoreCase("false")){
            flight.orElseThrow(() -> new GeneralException(ExceptionsEnum.PLANE_NOT_FOUND))
                    .setFly(isFly.equalsIgnoreCase("true"));
            flightRepository.save(flight.get());
            return ResponseEntity.ok().body("Flight updated!");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body isFly => " + isFly + " is not true! ");
        }
    }
}
