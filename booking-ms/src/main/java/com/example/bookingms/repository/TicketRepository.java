package com.example.bookingms.repository;

import com.example.bookingms.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findAllByUsername(String username);
    Optional<Ticket> findByIdAndUsername(Long ticketId, String username);
}
