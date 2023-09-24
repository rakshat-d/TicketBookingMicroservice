package org.movieBooking.Services;

import org.movieBooking.DTO.TicketRequest;
import org.movieBooking.Entities.Ticket;
import org.movieBooking.Exceptions.IdNotFoundException;

import java.util.List;

public interface BookingService {
    Ticket createTicket(TicketRequest ticketRequest) throws IdNotFoundException;
    Ticket getTicketById(Long id) throws IdNotFoundException;
    List<Ticket> getTicketsByUserId(Long userId);
    void deleteTicketById(Long id) throws IdNotFoundException;
}

