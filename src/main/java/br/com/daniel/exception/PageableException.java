package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class PageableException extends WebException {
    public PageableException(final String message) {
        this(message, null);
    }

    public PageableException(final String message, final String redirect) {
        super(message, "paginating-error", HttpStatus.INTERNAL_SERVER_ERROR, redirect);
    }
}
