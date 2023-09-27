package org.movieBooking.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.annotation.ServletSecurity;
import org.movieBooking.dto.ErrorResponse;
import org.movieBooking.dto.Error;
import org.movieBooking.exceptions.AuthenticationFailedException;
import org.movieBooking.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInvalidAccountNumberException(UsernameAlreadyExistsException e) {
        var error = Error.builder().errorCode("Username.Taken").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.CONFLICT.name()).errors(List.of(error)).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        var error = Error.builder().errorCode("Authentication.Failed").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.name()).message("Failed to authenticate").errors(List.of(error)).build();
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtExpiration(JwtException e) {
        var error = Error.builder().errorCode("Jwt.Expired").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.name()).message("Error Validating JWT Token").errors(List.of(error)).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        var fieldErrors = result.getFieldErrors();
        var errors = processFieldErrors(fieldErrors);
        return ErrorResponse.builder().code(BAD_REQUEST.name()).errors(errors).build();
    }

    private List<Error> processFieldErrors(List<FieldError> fieldErrors) {
        List<Error> errors = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            errors.add(Error.builder().errorCode("Validation.Failed").message(fieldError.getDefaultMessage()).path(fieldError.getField()).build());
        }
        return errors;
    }
}
