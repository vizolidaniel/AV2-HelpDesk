package br.com.daniel.security.service;

import br.com.daniel.model.Response;
import br.com.daniel.security.domain.Role;
import br.com.daniel.security.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    public Response<Role> findAllRoles() {
        final Set<Role> roles = this.repository.findAllRoles();
        return new Response<Role>().builder().results(roles).page(1).build();
    }

    public Set<Role> findRolesByIds(final Set<String> ids) {
        return this.repository.findRolesByIdIn(ids);
    }
}
