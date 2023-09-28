package org.movieBooking.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.movieBooking.dto.TicketRequest;
import org.movieBooking.entities.Ticket;
import org.movieBooking.services.BookingService;
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

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    /**
     * Method under test: {@link BookingController#bookTicket(TicketRequest)}
     */
    @Test
    void testBookTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setMovieId(1L);
        ticket.setMovieTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        ticket.setScreenNumber(10);
        ticket.setSeatNumber(10);
        ticket.setTheatreId(1L);
        ticket.setUserId(1L);
        when(bookingService.createTicket(Mockito.<TicketRequest>any())).thenReturn(ticket);

        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setMovieId(1L);
        ticketRequest.setTheatreId(1L);
        ticketRequest.setUserId(1L);
        String content = (new ObjectMapper()).writeValueAsString(ticketRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"theatreId\":1,\"movieId\":1,\"userId\":1,\"screenNumber\":10,\"seatNumber\":10,\"movieTime\":[1970"
                                        + ",1,1,0,0]}"));
    }

    /**
     * Method under test: {@link BookingController#deleteTicket(Long)}
     */
    @Test
    void testDeleteTicket() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    /**
     * Method under test: {@link BookingController#getTicket(Long)}
     */
    @Test
    void testGetTicket() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }
}

