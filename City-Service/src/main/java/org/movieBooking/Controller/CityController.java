package org.movieBooking.Controller;

import org.movieBooking.DTO.CityRequest;
import org.movieBooking.Entities.City;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CityController {

    @Autowired private CityService service;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public City getCityById(@PathVariable Long id) throws IdNotFoundException {
        return service.getCityById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public City createCity(@RequestBody CityRequest cityRequest) throws DuplicateEntryException {
        return service.createCity(cityRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public City updateCityById(@PathVariable Long id, @RequestBody CityRequest cityRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateCity(id, cityRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCityById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteCity(id);
    }

}
