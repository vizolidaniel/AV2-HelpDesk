package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends WebException {
    public ForbiddenException(final String message) {
        this(message, "/");
    }

    public ForbiddenException(final String message, final String redirect) {
        super(message, "forbidden", HttpStatus.FORBIDDEN, redirect);
    }
}
