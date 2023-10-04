package org.movieBooking.controller;

import org.movieBooking.annotations.HasRoles;
import org.movieBooking.dto.TheatreRequest;
import org.movieBooking.dto.TheatreResponse;
import org.movieBooking.entities.Theatre;
import org.movieBooking.enums.UserRole;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.services.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TheatreController {

    @Autowired private TheatreService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @HasRoles({UserRole.ADMIN})
    public Theatre createTheatre(@RequestBody TheatreRequest theatreRequest) throws DuplicateEntryException {
        return service.createTheatre(theatreRequest);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @HasRoles({UserRole.USER})
    public List<Theatre> getByCityId(@RequestParam Long cityId) {
        return service.getByCityId(cityId);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasRoles({UserRole.USER})
    public TheatreResponse getTheatreById(@PathVariable Long id) throws IdNotFoundException {
        return service.getTheatreById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasRoles({UserRole.ADMIN})
    public Theatre updateTheatreById(@PathVariable Long id, @RequestBody TheatreRequest theatreRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateTheatre(id, theatreRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasRoles({UserRole.ADMIN})
    public void deleteTheatreById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteTheatre(id);
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTheatresByCityId(@RequestParam Long id) {
        service.deleteByCityId(id);
    }


}
