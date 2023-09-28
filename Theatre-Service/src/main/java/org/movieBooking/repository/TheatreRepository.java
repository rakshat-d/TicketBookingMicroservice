package org.movieBooking.repository;

import org.movieBooking.entities.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Optional<Theatre> findByName(String name);
    boolean existsByName(String name);
    List<Theatre> findAllByCityId(Long cityId);
    void deleteByCityId(Long cityId);
}
