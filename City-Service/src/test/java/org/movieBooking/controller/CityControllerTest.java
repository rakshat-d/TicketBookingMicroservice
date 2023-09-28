package org.movieBooking.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.movieBooking.dto.CityRequest;
import org.movieBooking.entities.City;
import org.movieBooking.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CityController.class})
@ExtendWith(SpringExtension.class)
class CityControllerTest {
    @Autowired
    private CityController cityController;

    @MockBean
    private CityService cityService;

    /**
     * Method under test: {@link CityController#getCityById(Long)}
     */
    @Test
    void testGetCityById() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Name");
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(city);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{id}", 1L);
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test: {@link CityController#createCity(CityRequest)}
     */
    @Test
    void testCreateCity() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Name");
        when(cityService.createCity(Mockito.<CityRequest>any())).thenReturn(city);

        CityRequest cityRequest = new CityRequest();
        cityRequest.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test: {@link CityController#deleteCityById(Long)}
     */
    @Test
    void testDeleteCityById() throws Exception {
        doNothing().when(cityService).deleteCity(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CityController#updateCityById(Long, CityRequest)}
     */
    @Test
    void testUpdateCityById() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Name");
        when(cityService.updateCity(Mockito.<Long>any(), Mockito.<CityRequest>any())).thenReturn(city);

        CityRequest cityRequest = new CityRequest();
        cityRequest.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }
}

