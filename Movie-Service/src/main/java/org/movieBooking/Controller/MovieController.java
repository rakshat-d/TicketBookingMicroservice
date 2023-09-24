package org.movieBooking.Controller;

import jakarta.ws.rs.QueryParam;
import org.movieBooking.DTO.MovieRequest;
import org.movieBooking.Entities.Movie;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired private MovieService service;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Movie getMovieById(@PathVariable Long id) throws IdNotFoundException {
        return service.getMovieById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody MovieRequest movieRequest) throws DuplicateEntryException {
        return service.createMovie(movieRequest);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMovie(@RequestParam List<Long> id) {
        System.out.println(id);
        return service.getMovies(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Movie updateMovieById(@PathVariable Long id, @RequestBody MovieRequest movieRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateMovie(id, movieRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovieById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteMovie(id);
    }

}
