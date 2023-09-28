package org.movieBooking.controller;

import org.movieBooking.dto.TicketRequest;
import org.movieBooking.entities.Ticket;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {
    @Autowired
    BookingService service;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Ticket bookTicket(@RequestBody TicketRequest ticketRequest) throws IdNotFoundException {
        return service.createTicket(ticketRequest);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> getTickets(@RequestParam Long userId) {
        return service.getTicketsByUserId(userId);
    }

    @GetMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public Ticket getTicket(@PathVariable Long ticketId) throws IdNotFoundException {
        return service.getTicketById(ticketId);
    }

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long ticketId) throws IdNotFoundException {
        service.deleteTicketById(ticketId);
    }
}
