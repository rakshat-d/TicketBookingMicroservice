package org.movieBooking.services;

import org.movieBooking.dto.TheatreRequest;
import org.movieBooking.dto.TheatreResponse;
import org.movieBooking.entities.Theatre;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;

import java.util.List;

public interface TheatreService {
    TheatreResponse getTheatreById(Long id) throws IdNotFoundException;
    Theatre createTheatre(TheatreRequest theatreRequest) throws DuplicateEntryException;

    Theatre updateTheatre(Long id, TheatreRequest theatreRequest) throws DuplicateEntryException, IdNotFoundException;

    void deleteTheatre(Long id) throws IdNotFoundException;

    void addMovie(Long id, Long movieId) throws IdNotFoundException;
    void deleteMovie(Long id, Long movieId) throws IdNotFoundException;
    List<Theatre> getByCityId(Long cityId);
    void deleteByCityId(Long cityId);
}
