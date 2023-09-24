package com.movieBooking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movieBooking.DTO.CityRequest;
import org.movieBooking.Entities.City;
import org.movieBooking.Exceptions.DuplicateEntryException;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Repository.CityRepository;
import org.movieBooking.Services.CityService;
import org.movieBooking.Services.CityServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    CityRepository repository;

    @InjectMocks
    CityService service = new CityServiceImpl();

    private static final Long cityId = 1L;
    private static final String cityName = "ABC";
    private static final String updatedCityName = "DEF";
    private static final CityRequest inputCityData = CityRequest.builder().name(cityName).build();
    private static final CityRequest updateCityData = CityRequest.builder().name(updatedCityName).build();
    private static final City saveCity = City.builder().id(0L).name(cityName).build();

    private static final City expectedResponse = City.builder().id(cityId).name(cityName).build();
    private static final City updateExpectedResponse = City.builder().id(cityId).name(updatedCityName).build();


    @Test
    public void testCreateCitySuccess() throws DuplicateEntryException {
        when(repository.save(any())).thenReturn(expectedResponse);
        when(repository.existsByName(cityName)).thenReturn(false);
        var response = service.createCity(inputCityData);
        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(response.getName()).isEqualTo(cityName);

        verify(repository, times(1)).save(any());
        verify(repository, times(1)).existsByName(cityName);
    }

    @Test
    public void testCreateFailsDuplicateEntry() {
        when(repository.existsByName(cityName)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> service.createCity(inputCityData))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessage("City with name: " + inputCityData.getName() + " already exists");

        verify(repository, times(1)).existsByName(cityName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void testGetByIdSuccess() throws IdNotFoundException {
        when(repository.findById(cityId)).thenReturn(Optional.of(expectedResponse));
        var response = service.getCityById(cityId);
        Assertions.assertThat(response.getId()).isEqualTo(cityId);
        Assertions.assertThat(response.getName()).isEqualTo(cityName);

        verify(repository, times(1)).findById(cityId);
    }

    @Test
    public void testGetByIdFailureIdNotFound() {
        when(repository.findById(cityId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.getCityById(cityId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage("City with Id: " + cityId + " does not exists");

        verify(repository, times(1)).findById(cityId);
    }

    @Test
    public void testUpdateByIdSuccess() throws DuplicateEntryException, IdNotFoundException {
        when(repository.existsByName(updatedCityName)).thenReturn(false);
        when(repository.findById(cityId)).thenReturn(Optional.of(expectedResponse));
        when(repository.save(any())).thenReturn(updateExpectedResponse);
        var response = service.updateCity(cityId, updateCityData);
       Assertions.assertThat(response.getId()).isEqualTo(cityId);
       Assertions.assertThat(response.getName()).isEqualTo(updatedCityName);

       verify(repository, times(1)).findById(cityId);
       verify(repository, times(1)).existsByName(updatedCityName);
       verify(repository, times(1)).save(any());
    }

    @Test
    public void testUpdateByIdFailureDuplicate() throws IdNotFoundException {
        when(repository.existsByName(updatedCityName)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> service.updateCity(cityId, updateCityData))
                        .isInstanceOf(DuplicateEntryException.class)
                                .hasMessage("City with name: " + updatedCityName + " already exists");

        verify(repository, times(0)).findById(cityId);
        verify(repository, times(1)).existsByName(updatedCityName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void testUpdateByIdFailureIdNotFound() throws DuplicateEntryException {
        when(repository.existsByName(updatedCityName)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> service.updateCity(cityId, updateCityData))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage("City with Id: " + cityId + " does not exists");

        verify(repository, times(1)).findById(cityId);
        verify(repository, times(1)).existsByName(updatedCityName);
        verify(repository, times(0)).save(any());
    }

    @Test
    public void deleteByIdSuccess() throws IdNotFoundException {
        when(repository.existsById(cityId)).thenReturn(true);
        service.deleteCity(cityId);
        verify(repository, times(1)).existsById(cityId);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    public void deleteByIdFailureIdNotFound() {
        when(repository.existsById(cityId)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> service.deleteCity(cityId))
                        .isInstanceOf(IdNotFoundException.class)
                                .hasMessage("City with id: " + cityId + " does not exists");
        verify(repository, times(1)).existsById(cityId);
        verify(repository, times(0)).deleteById(any());
    }
}
