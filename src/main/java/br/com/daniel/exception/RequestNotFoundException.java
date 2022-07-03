package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class RequestNotFoundException extends WebException {
    public RequestNotFoundException(final String id) {
        super(String.format("Chamdo não encontrado: %s", id), "user-not-found", HttpStatus.NOT_FOUND, "/requests");
    }
}
