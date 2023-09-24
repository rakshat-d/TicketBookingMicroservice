package org.movieBooking.Services;

import org.movieBooking.DTO.CityRequest;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Entities.City;

public interface CityService {
    City getCityById(Long id) throws IdNotFoundException;
    City createCity(CityRequest cityRequest) throws DuplicateEntryException;

    City updateCity(Long id, CityRequest cityRequest) throws DuplicateEntryException, IdNotFoundException;

    void deleteCity(Long id) throws IdNotFoundException;
}
