package org.movieBooking.Services;

import org.movieBooking.DTO.TheatreRequest;
import org.movieBooking.DTO.TheatreResponse;
import org.movieBooking.Entities.Theatre;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;

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
