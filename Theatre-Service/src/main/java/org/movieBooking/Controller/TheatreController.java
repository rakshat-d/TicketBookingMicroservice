package org.movieBooking.Controller;

import org.movieBooking.DTO.TheatreRequest;
import org.movieBooking.DTO.TheatreResponse;
import org.movieBooking.Entities.Theatre;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Services.TheatreService;
import org.movieBooking.Services.TheatreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TheatreController {

    @Autowired private TheatreService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Theatre createTheatre(@RequestBody TheatreRequest theatreRequest) throws DuplicateEntryException {
        return service.createTheatre(theatreRequest);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Theatre> getByCityId(@RequestParam Long cityId) {
        return service.getByCityId(cityId);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TheatreResponse getTheatreById(@PathVariable Long id) throws IdNotFoundException {
        return service.getTheatreById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Theatre updateTheatreById(@PathVariable Long id, @RequestBody TheatreRequest theatreRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateTheatre(id, theatreRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheatreById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteTheatre(id);
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTheatresByCityId(@RequestParam Long id) {
        service.deleteByCityId(id);
    }


}
