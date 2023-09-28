package org.movieBooking.services;

import org.movieBooking.dto.TicketRequest;
import org.movieBooking.entities.Ticket;
import org.movieBooking.exceptions.IdNotFoundException;

import java.util.List;

public interface BookingService {
    Ticket createTicket(TicketRequest ticketRequest) throws IdNotFoundException;
    Ticket getTicketById(Long id) throws IdNotFoundException;
    List<Ticket> getTicketsByUserId(Long userId);
    void deleteTicketById(Long id) throws IdNotFoundException;
}

