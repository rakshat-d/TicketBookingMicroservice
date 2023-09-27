package org.movieBooking.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.movieBooking.dto.Error;
import org.movieBooking.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("trying to filter");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception");
            ErrorResponse errorResponse = new ErrorResponse();
            if (e instanceof AuthenticationException) {
                log.error("Auth error");
               var error = Error.builder().errorCode("Authentication.Failed").message(e.getMessage()).build();
               errorResponse = ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.name()).message("Failed to authenticate").errors(List.of(error)).build();
                response.setStatus(HttpStatus.CONFLICT.value());
            } else if (e instanceof JwtException) {
                log.error("Jwt error");
                var error = Error.builder().errorCode("Jwt.Expired").message(e.getMessage()).build();
                errorResponse = ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.name()).message("Error Validating JWT Token").errors(List.of(error)).build();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            response.getWriter().write(convertObjectToJson(errorResponse));
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return "";
        }

        return mapper.writeValueAsString(object);
    }
}
