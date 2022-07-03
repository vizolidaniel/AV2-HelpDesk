package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class DatabaseConflictException extends WebException {
    public DatabaseConflictException(String message, String redirect) {
        super(message, "conflict", HttpStatus.CONFLICT, redirect);
    }
}
