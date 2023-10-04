package org.movieBooking.controller;

import org.movieBooking.annotations.HasRoles;
import org.movieBooking.dto.MovieRequest;
import org.movieBooking.entities.Movie;
import org.movieBooking.enums.UserRole;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired private MovieService service;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasRoles({UserRole.USER})
    public Movie getMovieById(@PathVariable Long id) throws IdNotFoundException {
        return service.getMovieById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @HasRoles({UserRole.ADMIN})
    public Movie createMovie(@RequestBody MovieRequest movieRequest) throws DuplicateEntryException {
        return service.createMovie(movieRequest);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @HasRoles({UserRole.ADMIN})
    public List<Movie> getMovie(@RequestParam List<Long> id) {
        System.out.println(id);
        return service.getMovies(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasRoles({UserRole.ADMIN})
    public Movie updateMovieById(@PathVariable Long id, @RequestBody MovieRequest movieRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateMovie(id, movieRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasRoles({UserRole.ADMIN})
    public void deleteMovieById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteMovie(id);
    }

}
