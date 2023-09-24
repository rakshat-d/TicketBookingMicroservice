package org.movieBooking.Repository;

import org.movieBooking.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
}
