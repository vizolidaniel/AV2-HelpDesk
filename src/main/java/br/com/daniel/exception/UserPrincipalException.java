package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class UserPrincipalException extends WebException {
    public UserPrincipalException() {
        super("E-mail ou senha inválidos", "unauthorized", HttpStatus.UNAUTHORIZED, "/login");
    }
}
