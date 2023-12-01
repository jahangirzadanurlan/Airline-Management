package com.example.flightms.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "booking-ms",url = "http://localhost:8086")
public interface TicketClient {

    @GetMapping("/booking/sale-ticket-count")
    int getTicketSaleCount();

    @PostMapping("/booking/sale-ticket-count")
    void changeSaleTicketCount();

}
