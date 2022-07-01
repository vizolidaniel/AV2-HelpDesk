package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class UserPrincipalException extends WebException {
    public UserPrincipalException() {
        this(null);
    }

    public UserPrincipalException(final String redirect) {
        super("E-mail ou senha inválidos", "unauthorized", HttpStatus.UNAUTHORIZED, redirect);
    }
}
