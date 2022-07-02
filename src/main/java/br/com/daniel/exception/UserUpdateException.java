package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class UserUpdateException extends WebException {
    public UserUpdateException(final String message, final String redirect) {
        super(message, "bad-request", HttpStatus.BAD_REQUEST, redirect);
    }
}
