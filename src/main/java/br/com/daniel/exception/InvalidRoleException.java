package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

public class InvalidRoleException extends WebException {
    public InvalidRoleException(final String roleRule) {
        super(String.format("Regra de permissão inválida: %s", roleRule), "forbidden", HttpStatus.FORBIDDEN, "/login");
    }
}
