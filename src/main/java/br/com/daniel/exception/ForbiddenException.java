package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends WebException {
    public ForbiddenException() {
        this(null);
    }

    public ForbiddenException(final String redirect) {
        super("username can not access this resource", "forbidden", HttpStatus.FORBIDDEN, redirect);
    }
}
