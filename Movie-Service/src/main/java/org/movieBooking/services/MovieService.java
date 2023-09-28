package org.movieBooking.services;

import org.movieBooking.dto.MovieRequest;
import org.movieBooking.entities.Movie;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;

import java.util.List;

public interface MovieService {
    Movie getMovieById(Long id) throws IdNotFoundException;
    Movie createMovie(MovieRequest movieRequest) throws DuplicateEntryException;

    Movie updateMovie(Long id, MovieRequest movieRequest) throws DuplicateEntryException, IdNotFoundException;

    void deleteMovie(Long id) throws IdNotFoundException;

    List<Movie> getMovies(List<Long> ids);
}
