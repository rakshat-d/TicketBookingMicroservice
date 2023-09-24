package org.movieBooking.Services;

import org.movieBooking.DTO.MovieRequest;
import org.movieBooking.Entities.Movie;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;

import java.util.List;

public interface MovieService {
    Movie getMovieById(Long id) throws IdNotFoundException;
    Movie createMovie(MovieRequest movieRequest) throws DuplicateEntryException;

    Movie updateMovie(Long id, MovieRequest movieRequest) throws DuplicateEntryException, IdNotFoundException;

    void deleteMovie(Long id) throws IdNotFoundException;

    List<Movie> getMovies(List<Long> ids);
}
