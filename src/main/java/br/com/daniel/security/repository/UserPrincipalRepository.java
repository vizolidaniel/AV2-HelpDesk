package br.com.daniel.security.repository;

import br.com.daniel.security.domain.Role;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserPrincipalRepository {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserPrincipalRepository(
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final UserRoleRepository userRoleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserPrincipal> findByEmail(final String email) {
        return this.userRepository.findByEmail(email)
                .map(user -> {
                    final Set<UserRole> userRoles = this.userRoleRepository.findUserRolesByUserId(user.getId());
                    Set<Role> roles = new HashSet<>();
                    if (!userRoles.isEmpty())
                        roles = this.roleRepository.findRolesByIdIn(userRoles
                                .stream()
                                .map(UserRole::getRoleId)
                                .collect(Collectors.toSet()));
                    return new UserPrincipal(user, roles);
                });
    }
}
