package org.movieBooking.exceptions;

public class AuthorizationFailedException extends Exception {
    public AuthorizationFailedException(String message) {
        super(message);
    }
}
