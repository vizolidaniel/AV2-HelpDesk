package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends WebException {
    public UserNotFoundException(final String idOrEmail, final String redirect) {
        super(String.format("Usuário não encontrado: %s", idOrEmail), "user-not-found", HttpStatus.NOT_FOUND, redirect);
    }
}
