package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class TooLessAdminsException extends WebException {
    public TooLessAdminsException() {
        super("Não é permitido remover o unico usuário ADMIN", "forbidden", HttpStatus.FORBIDDEN, "/users");
    }
}
