package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public abstract class WebException extends RuntimeException {
    private final HttpStatus status;
    private final String logref;

    private final String redirect;

    public WebException(final String message, final String logref, final HttpStatus status, final String redirect) {
        super(message);
        this.status = status;
        this.logref = logref;
        this.redirect = redirect;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getLogref() {
        return this.logref;
    }

    public String getRedirect() {
        return this.redirect;
    }
}
