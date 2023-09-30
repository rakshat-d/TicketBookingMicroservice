package org.movieBooking.handler;

import org.movieBooking.dto.ErrorResponse;
import org.movieBooking.dto.Error;
import org.movieBooking.exceptions.AuthorizationFailedException;
import org.movieBooking.exceptions.DuplicateEntryException;
import org.movieBooking.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse HandleItemNotFoundException(IdNotFoundException e) {
        var error = Error.builder().errorCode("Not.Found").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.NOT_FOUND.name()).message("Id Not found").errors(List.of(error)).build();
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse HandleDuplicateEntryException(DuplicateEntryException e) {
        var error = Error.builder().errorCode("Duplicate.Entry").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.CONFLICT.name()).message("Item Already Exists").errors(List.of(error)).build();
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse HandleAuthorizationFailed(AuthorizationFailedException e) {
        var error = Error.builder().errorCode("Not.Authorized").message(e.getMessage()).build();
        return ErrorResponse.builder().code(HttpStatus.FORBIDDEN.name()).message("You are not authorized to access this resource").errors(List.of(error)).build();
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

    private List<Error> processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        List<Error> errors = new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            errors.add(Error.builder().errorCode("Validation.Failed").message(fieldError.getDefaultMessage()).path(fieldError.getField()).build());
        }
        return errors;
    }

}