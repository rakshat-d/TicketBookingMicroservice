package org.movieBooking.Services;

import jakarta.persistence.Id;
import org.movieBooking.DTO.CityRequest;
import org.movieBooking.Entities.City;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Repository.CityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CityServiceImpl implements CityService{

    @Autowired
    WebClient.Builder webClient;

    @Autowired
    CityRepository repository;
    @Override
    public City getCityById(Long id) throws IdNotFoundException {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException("City with Id: " + id + " does not exists"));
    }

    @Override
    public City createCity(CityRequest cityRequest) throws DuplicateEntryException {
        if (repository.existsByName(cityRequest.getName())) throw new DuplicateEntryException("City with name: " + cityRequest.getName() + " already exists");
        var city = new City();
        BeanUtils.copyProperties(cityRequest, city);
        return repository.save(city);
    }

    @Override
    public City updateCity(Long id, CityRequest cityRequest) throws DuplicateEntryException, IdNotFoundException {
        if (repository.existsByName(cityRequest.getName())) throw new DuplicateEntryException("City with name: " + cityRequest.getName() + " already exists");
        var city = repository.findById(id).orElseThrow(() -> new IdNotFoundException("City with Id: " + id + " does not exists"));
        BeanUtils.copyProperties(cityRequest, city);
        return repository.save(city);
    }

    @Override
    public void deleteCity(Long id) throws IdNotFoundException {
        if (!repository.existsById(id)) throw new IdNotFoundException("City with id: " + id + " does not exists");
        var client = webClient.baseUrl("http://theatre-service").build();
        client
                        .delete()
                        .uri("/api/v1/movie-booking/theatre?cityId="+id)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                        .bodyToMono(Void.class)
                        .block();
        repository.deleteById(id);
    }
}
