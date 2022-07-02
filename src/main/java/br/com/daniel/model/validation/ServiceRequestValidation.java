package br.com.daniel.model.validation;

import br.com.daniel.exception.MissingParamException;
import br.com.daniel.model.dto.ServiceRequestDTO;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ServiceRequestValidation {
    private ServiceRequestValidation() {
    }

    public static void validate(final ServiceRequestDTO dto) {
        final Set<String> missingParams = new HashSet<>();

        if (!StringUtils.hasText(dto.getDescription())) missingParams.add("Descrição");

        if (!missingParams.isEmpty()) throw new MissingParamException(missingParams, "/requests/create");
    }
}
