package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class CanNotSelfDeleteException extends WebException {
    public CanNotSelfDeleteException() {
        super("Não é permitido remover o seu próprio usuário", "forbidden", HttpStatus.FORBIDDEN, "/users");
    }
}
