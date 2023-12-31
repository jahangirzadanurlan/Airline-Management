package com.example.commonms.repository;

import com.example.commonms.model.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Airline,Long> {
    List<Airline> findAllByAirlineNameContainingIgnoreCase(String name);
    List<Airline> findAllByCountryContainingIgnoreCase(String country);
}
