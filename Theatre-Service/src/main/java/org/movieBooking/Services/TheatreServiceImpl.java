package org.movieBooking.Services;

import jakarta.ws.rs.core.UriBuilder;
import org.movieBooking.DTO.Movie;
import org.movieBooking.DTO.TheatreRequest;
import org.movieBooking.DTO.TheatreResponse;
import org.movieBooking.Entities.Theatre;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Repository.TheatreRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    TheatreRepository repository;

    @Autowired
    WebClient.Builder webClient;
    @Override
    public TheatreResponse getTheatreById(Long id) throws IdNotFoundException {
        var theatre = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Theatre with Id: " + id + " does not exists"));
        var response = new TheatreResponse();
        BeanUtils.copyProperties(theatre, response);
        if (theatre.getMovies() != null && !theatre.getMovies().isEmpty())
            response.setMovies(getMoviesById(theatre.getMovies()));
        return response;
    }

    @Override
    public Theatre createTheatre(TheatreRequest theatreRequest)  {
        var Theatre = new Theatre();
        BeanUtils.copyProperties(theatreRequest, Theatre);
        return repository.save(Theatre);
    }

    @Override
    public Theatre updateTheatre(Long id, TheatreRequest theatreRequest) throws IdNotFoundException {
        var Theatre = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Theatre with Id: " + id + " does not exists"));
        BeanUtils.copyProperties(theatreRequest, Theatre);
        return repository.save(Theatre);
    }

    @Override
    public void deleteTheatre(Long id) throws IdNotFoundException {
        if (!repository.existsById(id)) throw new IdNotFoundException("Theatre with id: " + id + " does not exists");
        repository.deleteById(id);
    }

    @Override
    public void addMovie(Long id, Long movieId) throws IdNotFoundException {
        var theatre = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Theatre with Id: " + id + " does not exists"));
        theatre.getMovies().add(movieId);
        repository.save(theatre);
    }

    @Override
    public void deleteMovie(Long id, Long movieId) throws IdNotFoundException {
        var theatre = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Theatre with Id: " + id + " does not exists"));
        theatre.getMovies().remove(movieId);
    }

    @Override
    public List<Theatre> getByCityId(Long cityId) {
        return repository.findAllByCityId(cityId);
    }

    @Override
    public void deleteByCityId(Long cityId) {
        repository.deleteByCityId(cityId);
    }


    public List<Movie> getMoviesById(List<Long> ids) {
        var idStr = ids.stream().map(Object::toString).toList();
        var uri = UriComponentsBuilder.newInstance()
                .host("http")
                .host("movie-service")
                .path("/api/v1/movie-booking/movie/")
                .queryParam("id", idStr)
                .build();
        Movie[] movies = webClient.build().get()
                .uri(uri.toUri())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .bodyToMono(Movie[].class)
                .block();
        if (movies == null) throw new RuntimeException("Error while fetching movies");
        return Arrays.stream(movies)
                .filter(Objects::nonNull)
                .toList();
    }
}
