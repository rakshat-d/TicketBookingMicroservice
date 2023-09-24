package org.movieBooking.Repository;

import org.movieBooking.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);
    boolean existsByName(String name);
}
