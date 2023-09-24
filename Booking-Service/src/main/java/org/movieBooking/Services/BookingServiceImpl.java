package org.movieBooking.Services;

import org.movieBooking.DTO.Theatre;
import org.movieBooking.DTO.TheatreResponse;
import org.movieBooking.DTO.TicketRequest;
import org.movieBooking.Entities.Ticket;
import org.movieBooking.Exceptions.IdNotFoundException;
import org.movieBooking.Repository.BookingRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired
    WebClient.Builder webClient;

    @Override
    public Ticket createTicket(TicketRequest ticketRequest) throws IdNotFoundException {
        var theatre = getTheatre(ticketRequest.getTheatreId());
        var movie = theatre.getMovies().stream().filter(m -> Objects.equals(m.getId(), ticketRequest.getMovieId()))
                .reduce((a, b) -> null).orElseThrow(() -> new RuntimeException("Theatre with id: " + ticketRequest.getTheatreId() + " has no shows for movie id: " + ticketRequest.getMovieId()));
        var ticket = new Ticket();
        BeanUtils.copyProperties(ticketRequest, ticket);
        ticket.setScreenNumber(RandomGenerator.getDefault().nextInt(1, 10));
        ticket.setSeatNumber(RandomGenerator.getDefault().nextInt(1, 200));
        ticket.setMovieTime(LocalDateTime.now());
        BeanUtils.copyProperties(ticketRequest, ticket);
        return bookingRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) throws IdNotFoundException {
        return bookingRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Theatre with id: " + id + " does not exists"));
    }

    @Override
    public void deleteTicketById(Long id) throws IdNotFoundException {
        if (!bookingRepository.existsById(id)) throw new IdNotFoundException("Theatre with id: " + id + " does not exists");
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Ticket> getTicketsByUserId(Long userID) {
        return bookingRepository.findByUserId(userID);
    }

    private TheatreResponse getTheatre(Long id) {
        return webClient.build().get()
                .uri("http://theatre-service/api/v1/movie-booking/theatre/"+id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .bodyToMono(TheatreResponse.class)
                .block();
    }
}
