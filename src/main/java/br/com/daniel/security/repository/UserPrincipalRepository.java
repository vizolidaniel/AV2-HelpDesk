package br.com.daniel.security.repository;

import br.com.daniel.model.Response;
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

    public Optional<UserPrincipal> findById(final String id) {
        return this.userRepository.findById(id)
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

    public Response<UserPrincipal> paginateAll(final int page, final int size) {
        return this.userRepository.findAll(page, size)
                .map(
                        user -> {
                            final Set<UserRole> userRoles = this.userRoleRepository.findUserRolesByUserId(user.getId());
                            Set<Role> roles = new HashSet<>();
                            if (!userRoles.isEmpty())
                                roles = this.roleRepository.findRolesByIdIn(userRoles
                                        .stream()
                                        .map(UserRole::getRoleId)
                                        .collect(Collectors.toSet()));
                            return new UserPrincipal(user, roles);
                        },
                        Collectors.toSet()
                );
    }

    public void updateUser(final UserPrincipal user) {
        this.userRepository.update(user);
        this.userRepository.findById(user.getId()).ifPresent(updatedUser -> {
            this.userRoleRepository.deleteByUserId(updatedUser.getId());
            this.userRoleRepository.insertRolesForUserId(user
                    .getRoles()
                    .stream()
                    .map(role -> new UserRole(updatedUser.getUpdatedBy(), updatedUser.getId(), role.getId()))
                    .collect(Collectors.toSet())
            );
        });
    }

    public int countAdmins() {
        final Role admin = this.roleRepository.getAdminRole();
        final Set<UserRole> userRoles = this.userRoleRepository.findAllByRoleId(admin.getId());
        final Set<String> usersIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());
        return usersIds.size();
    }

    public void deleteById(final String id) {
        this.userRoleRepository.deleteByUserId(id);
        this.userRepository.deleteById(id);
    }

    public void insertUser(final UserPrincipal user) {
        this.userRepository.insert(user);
        this.userRoleRepository.insertRolesForUserId(user
                .getRoles()
                .stream()
                .map(role -> new UserRole(user.getUpdatedBy(), user.getId(), role.getId()))
                .collect(Collectors.toSet())
        );
    }
}
