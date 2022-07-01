package br.com.daniel.exception;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class MissingParamException extends WebException {
    public MissingParamException(final Set<String> params) {
        this(params, null);
    }

    public MissingParamException(final Set<String> params, final String redirect) {
        super(
                String.format("Parametros inválidos: %s", String.join(",", params)),
                "missing-params",
                HttpStatus.BAD_REQUEST,
                redirect
        );
    }
}
