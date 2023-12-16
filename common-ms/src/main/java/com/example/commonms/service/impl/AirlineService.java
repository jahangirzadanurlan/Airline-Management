package com.example.commonms.service.impl;

import com.example.commonexception.enums.ExceptionsEnum;
import com.example.commonexception.exceptions.GeneralException;
import com.example.commonms.model.dto.request.AirlineRequestDto;
import com.example.commonms.model.entity.Airline;
import com.example.commonms.repository.AirlineRepository;
import com.example.commonms.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirlineService implements IAirlineService {
    private final AirlineRepository airlineRepository;

    @Override
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @Override
    public Airline getAirlineById(Long id) {
        Optional<Airline> airline = airlineRepository.findById(id);
        return airline.orElseThrow(() -> new GeneralException(ExceptionsEnum.AIRLINE_NOT_FOUND));
    }

    @Override
    public List<Airline> searchAirline(AirlineRequestDto airlineRequestDto) {
        if (airlineRequestDto.getAirlineName() != null && !airlineRequestDto.getAirlineName().isEmpty()){
            return airlineRepository.findAllByAirlineNameContainingIgnoreCase(airlineRequestDto.getAirlineName());
        }else if (airlineRequestDto.getCountry() != null && !airlineRequestDto.getCountry().isEmpty()){
            return airlineRepository.findAllByCountryContainingIgnoreCase(airlineRequestDto.getCountry());
        }else {
            throw new GeneralException(ExceptionsEnum.AIRLINE_NOT_FOUND);
        }
    }


}
