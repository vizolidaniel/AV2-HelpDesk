package br.com.daniel.model.validation;

import br.com.daniel.exception.MissingParamException;
import br.com.daniel.model.dto.ServiceRequestCommentDTO;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ServiceRequestCommentValidation {
    private ServiceRequestCommentValidation() {
    }

    public static void validate(final ServiceRequestCommentDTO dto, final String searchRequestId) {
        final Set<String> missingParams = new HashSet<>();

        if (!StringUtils.hasText(dto.getComment())) missingParams.add("Comentário");

        if (!missingParams.isEmpty())
            throw new MissingParamException(missingParams, String.format("/requests/%s", searchRequestId));
    }
}
