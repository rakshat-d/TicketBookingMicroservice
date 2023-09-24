package org.movieBooking.Services;

import org.movieBooking.DTO.MovieRequest;
import org.movieBooking.Entities.Movie;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository repository;
    @Override
    public Movie getMovieById(Long id) throws IdNotFoundException {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException("Movie with Id: " + id + " does not exists"));
    }

    @Override
    public Movie createMovie(MovieRequest movieRequest) throws DuplicateEntryException {
        if (repository.existsByName(movieRequest.getName())) throw new DuplicateEntryException("Movie with name: " + movieRequest.getName() + " already exists");
        var city = new Movie();
        BeanUtils.copyProperties(movieRequest, city);
        return repository.save(city);
    }

    @Override
    public Movie updateMovie(Long id, MovieRequest movieRequest) throws DuplicateEntryException, IdNotFoundException {
        if (repository.existsByName(movieRequest.getName())) throw new DuplicateEntryException("Movie with name: " + movieRequest.getName() + " already exists");
        var city = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Movie with Id: " + id + " does not exists"));
        BeanUtils.copyProperties(movieRequest, city);
        return repository.save(city);
    }

    @Override
    public void deleteMovie(Long id) throws IdNotFoundException {
        if (!repository.existsById(id)) throw new IdNotFoundException("Movie with id: " + id + " does not exists");
        repository.deleteById(id);
    }

    @Override
    public List<Movie> getMovies(List<Long> ids) {
        return repository.findAllById(ids);
    }
}
