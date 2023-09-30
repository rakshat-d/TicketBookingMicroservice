package org.movieBooking.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.movieBooking.annotations.HasRoles;
import org.movieBooking.dto.CityRequest;
import org.movieBooking.entities.City;
import org.movieBooking.enums.UserRole;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CityController {


    @Autowired private CityService service;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasRoles({UserRole.USER})
    public City getCityById(HttpServletRequest request, @PathVariable Long id) throws IdNotFoundException {
        return service.getCityById(id);
    }

    @PostMapping("/")
    @HasRoles({UserRole.ADMIN})
    @ResponseStatus(HttpStatus.CREATED)
    public City createCity(@RequestBody CityRequest cityRequest) throws DuplicateEntryException {
        return service.createCity(cityRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasRoles({UserRole.ADMIN})
    public City updateCityById(@PathVariable Long id, @RequestBody CityRequest cityRequest) throws DuplicateEntryException, IdNotFoundException {
        return service.updateCity(id, cityRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasRoles({UserRole.ADMIN})
    public void deleteCityById(@PathVariable Long id) throws IdNotFoundException {
        service.deleteCity(id);
    }

}
