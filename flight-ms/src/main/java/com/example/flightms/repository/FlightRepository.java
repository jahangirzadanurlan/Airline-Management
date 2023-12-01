package com.example.flightms.repository;

import com.example.flightms.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> {
    Flight findByAirplaneId(Long id);
}
