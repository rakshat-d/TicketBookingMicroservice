package movieBooking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movieBooking.dto.MovieRequest;
import org.movieBooking.entities.Movie;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.movieBooking.repository.MovieRepository;
import org.movieBooking.services.MovieService;
import org.movieBooking.services.MovieServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    MovieRepository repository;

    @InjectMocks
    MovieService service = new MovieServiceImpl();

    private static final Long movieId = 1L;
    private static final String movieName = "ABC";
    private static final String updatedMovieName = "DEF";
    private static final MovieRequest inputMovieData = MovieRequest.builder().name(movieName).build();
    private static final MovieRequest updateMovieData = MovieRequest.builder().name(updatedMovieName).build();
    private static final Movie saveMovie = Movie.builder().id(0L).name(movieName).build();

    private static final Movie expectedResponse = Movie.builder().id(movieId).name(movieName).build();
    private static final Movie updateExpectedResponse = Movie.builder().id(movieId).name(updatedMovieName).build();


    @Test
    public void testCreateMovieSuccess() throws DuplicateEntryException {
        when(repository.save(any())).thenReturn(expectedResponse);
        when(repository.existsByName(movieName)).thenReturn(false);
        var response = service.createMovie(inputMovieData);
        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(response.getName()).isEqualTo(movieName);

        verify(repository, times(1)).save(any());
        verify(repository, times(1)).existsByName(movieName);
    }

    @Test
    public void testCreateFailsDuplicateEntry() {
        when(repository.existsByName(movieName)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> service.createMovie(inputMovieData))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessage("Movie with name: " + inputMovieData.getName() + " already exists");

        verify(repository, times(1)).existsByName(movieName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void testGetByIdSuccess() throws IdNotFoundException {
        when(repository.findById(movieId)).thenReturn(Optional.of(expectedResponse));
        var response = service.getMovieById(movieId);
        Assertions.assertThat(response.getId()).isEqualTo(movieId);
        Assertions.assertThat(response.getName()).isEqualTo(movieName);

        verify(repository, times(1)).findById(movieId);
    }

    @Test
    public void testGetByIdFailureIdNotFound() {
        when(repository.findById(movieId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.getMovieById(movieId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage("Movie with Id: " + movieId + " does not exists");

        verify(repository, times(1)).findById(movieId);
    }

    @Test
    public void testUpdateByIdSuccess() throws DuplicateEntryException, IdNotFoundException {
        when(repository.existsByName(updatedMovieName)).thenReturn(false);
        when(repository.findById(movieId)).thenReturn(Optional.of(expectedResponse));
        when(repository.save(any())).thenReturn(updateExpectedResponse);
        var response = service.updateMovie(movieId, updateMovieData);
       Assertions.assertThat(response.getId()).isEqualTo(movieId);
       Assertions.assertThat(response.getName()).isEqualTo(updatedMovieName);

       verify(repository, times(1)).findById(movieId);
       verify(repository, times(1)).existsByName(updatedMovieName);
       verify(repository, times(1)).save(any());
    }

    @Test
    public void testUpdateByIdFailureDuplicate() throws IdNotFoundException {
        when(repository.existsByName(updatedMovieName)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> service.updateMovie(movieId, updateMovieData))
                        .isInstanceOf(DuplicateEntryException.class)
                                .hasMessage("Movie with name: " + updatedMovieName + " already exists");

        verify(repository, times(0)).findById(movieId);
        verify(repository, times(1)).existsByName(updatedMovieName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void testUpdateByIdFailureIdNotFound() throws DuplicateEntryException {
        when(repository.existsByName(updatedMovieName)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> service.updateMovie(movieId, updateMovieData))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage("Movie with Id: " + movieId + " does not exists");

        verify(repository, times(1)).findById(movieId);
        verify(repository, times(1)).existsByName(updatedMovieName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void deleteByIdSuccess() throws IdNotFoundException {
        when(repository.existsById(movieId)).thenReturn(true);
        service.deleteMovie(movieId);
        verify(repository, times(1)).existsById(movieId);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    public void deleteByIdFailureIdNotFound() {
        when(repository.existsById(movieId)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> service.deleteMovie(movieId))
                        .isInstanceOf(IdNotFoundException.class)
                                .hasMessage("Movie with id: " + movieId + " does not exists");
        verify(repository, times(1)).existsById(movieId);
        verify(repository, times(0)).deleteById(any());
    }
}
