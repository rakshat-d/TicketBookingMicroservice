package org.movieBooking.handler;

import org.movieBooking.dto.Error;
import org.movieBooking.dto.ErrorResponse;
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
    public ResponseEntity<String> HandleItemNotFoundException(IdNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<String> HandleDuplicateEntryException(DuplicateEntryException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        var fieldErrors = result.getFieldErrors();
        var errors = processFieldErrors(fieldErrors);
        var errorResponse = ErrorResponse.builder().code(BAD_REQUEST.name()).errors(errors).build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    private List<Error> processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        List<Error> errors = new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            errors.add(Error.builder().errorCode("Validation.Failed").message(fieldError.getDefaultMessage()).path(fieldError.getField()).build());
        }
        return errors;
    }

}