package br.com.daniel.model.mapper;

import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.model.dto.ServiceRequestDTO;

public class ServiceRequestMapper {
    private ServiceRequestMapper() {}

    public static ServiceRequest map(final ServiceRequestDTO dto, final String creator) {
        return new ServiceRequest(creator, dto.getDescription());
    }
}
