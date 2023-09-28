package org.movieBooking.repository;

import org.movieBooking.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
}
