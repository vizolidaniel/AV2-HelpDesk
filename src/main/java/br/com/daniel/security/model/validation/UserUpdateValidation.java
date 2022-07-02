package br.com.daniel.security.model.validation;

import br.com.daniel.exception.MissingParamException;
import br.com.daniel.security.model.dto.UserDTO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class UserUpdateValidation {
    private UserUpdateValidation() {}

    public static void validate(final UserDTO dto) {
        final Set<String> missingParams = new HashSet<>();

        if (!StringUtils.hasText(dto.getId())) missingParams.add("id");
        if (!StringUtils.hasText(dto.getName())) missingParams.add("Nome");
        if (!StringUtils.hasText(dto.getPassword())) missingParams.add("Senha");
        if (!StringUtils.hasText(dto.getEmail())) missingParams.add("E-mail");
        if (CollectionUtils.isEmpty(dto.getRoles())) missingParams.add("Permissões");

        if (!missingParams.isEmpty()) {
            final String redirect = missingParams.contains("id")
                    ? "/users"
                    : String.format("/users/update/%s", dto.getId());

            throw new MissingParamException(missingParams, redirect);
        }
    }
}
