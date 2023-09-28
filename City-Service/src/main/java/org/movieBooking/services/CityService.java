package org.movieBooking.services;

import org.movieBooking.dto.CityRequest;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.entities.City;

public interface CityService {
    City getCityById(Long id) throws IdNotFoundException;
    City createCity(CityRequest cityRequest) throws DuplicateEntryException;

    City updateCity(Long id, CityRequest cityRequest) throws DuplicateEntryException, IdNotFoundException;

    void deleteCity(Long id) throws IdNotFoundException;
}
