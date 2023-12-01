package com.example.flightms.service.impl;

import com.example.flightms.model.dto.response.PlaneResponseDto;
import com.example.flightms.model.entity.Flight;
import com.example.flightms.repository.FlightRepository;
import com.example.flightms.service.AirplaneClient;
import com.example.flightms.service.TicketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final TicketClient ticketClient;
    private final AirplaneClient airplaneClient;
    private final FlightRepository flightRepository;

    @Scheduled(fixedRate = 10000)
    public void changeTicketPrice(){
        changeTicketIncreasePercentByTicketSaleCount();

        flightRepository.findAll().stream()
                .map(Flight::getAirplaneId)
                .forEach(this::changeTicketIncreasePercentBySeatsCount);

    }

    private void changeTicketIncreasePercentByTicketSaleCount() {
        int ticketSaleCount = ticketClient.getTicketSaleCount();

        if (ticketSaleCount > 10){
            Flight.ticketIncreasePercentBYSaleTicketCount += 9;
        } else if (ticketSaleCount >= 2 && ticketSaleCount < 10) {
            Flight.ticketIncreasePercentBYSaleTicketCount += 5;
        } else if (ticketSaleCount == 1) {
            Flight.ticketIncreasePercentBYSaleTicketCount += 2;
        } else {
            Flight.ticketIncreasePercentBYSaleTicketCount -= 1;
        }

        ticketClient.changeSaleTicketCount();
    }

    private void changeTicketIncreasePercentBySeatsCount(Long airplaneId) {
        PlaneResponseDto planeResponseDto = airplaneClient.getAirplaneById(airplaneId);
        Flight flight = flightRepository.findByAirplaneId(airplaneId);

        int maxSeats = planeResponseDto.getMasSeats();
        int availableSeats = planeResponseDto.getAvailableSeats();

        if (availableSeats < (maxSeats * 5)/100){
            flight.setTicketIncreasePercentBYSeatsCount(23);
        } else if (availableSeats < (maxSeats * 10)/100) {
            flight.setTicketIncreasePercentBYSeatsCount(18);
        } else if (availableSeats < (maxSeats * 20)/100) {
            flight.setTicketIncreasePercentBYSeatsCount(11);
        } else if (availableSeats < (maxSeats * 30)/100) {
            flight.setTicketIncreasePercentBYSeatsCount(6);
        }

        flightRepository.save(flight);
    }


}
