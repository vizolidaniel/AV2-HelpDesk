package br.com.daniel.security.dao;

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
public class UserPrincipalDAO {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final UserRoleDAO userRoleDAO;

    public UserPrincipalDAO(
            final UserDAO userDAO,
            final RoleDAO roleDAO,
            final UserRoleDAO userRoleDAO
    ) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userRoleDAO = userRoleDAO;
    }

    public Optional<UserPrincipal> findByEmail(final String email) {
        return this.userDAO.findByEmail(email)
                .map(user -> {
                    final Set<UserRole> userRoles = this.userRoleDAO.findUserRolesByUserId(user.getId());
                    Set<Role> roles = new HashSet<>();
                    if (!userRoles.isEmpty())
                        roles = this.roleDAO.findRolesByIdIn(userRoles
                                .stream()
                                .map(UserRole::getRoleId)
                                .collect(Collectors.toSet()));
                    return new UserPrincipal(user, roles);
                });
    }

    public Optional<UserPrincipal> findById(final String id) {
        return this.userDAO.findById(id)
                .map(user -> {
                    final Set<UserRole> userRoles = this.userRoleDAO.findUserRolesByUserId(user.getId());
                    Set<Role> roles = new HashSet<>();
                    if (!userRoles.isEmpty())
                        roles = this.roleDAO.findRolesByIdIn(userRoles
                                .stream()
                                .map(UserRole::getRoleId)
                                .collect(Collectors.toSet()));
                    return new UserPrincipal(user, roles);
                });
    }

    public Response<UserPrincipal> paginateAll(final int page, final int size) {
        return this.userDAO.findAll(page, size)
                .map(
                        user -> {
                            final Set<UserRole> userRoles = this.userRoleDAO.findUserRolesByUserId(user.getId());
                            Set<Role> roles = new HashSet<>();
                            if (!userRoles.isEmpty())
                                roles = this.roleDAO.findRolesByIdIn(userRoles
                                        .stream()
                                        .map(UserRole::getRoleId)
                                        .collect(Collectors.toSet()));
                            return new UserPrincipal(user, roles);
                        },
                        Collectors.toSet()
                );
    }

    public void updateUser(final UserPrincipal user) {
        this.userDAO.update(user);
        this.userDAO.findById(user.getId()).ifPresent(updatedUser -> {
            this.userRoleDAO.deleteByUserId(updatedUser.getId());
            this.userRoleDAO.insertRolesForUserId(user
                    .getRoles()
                    .stream()
                    .map(role -> new UserRole(updatedUser.getUpdatedBy(), updatedUser.getId(), role.getId()))
                    .collect(Collectors.toSet())
            );
        });
    }

    public int countAdmins() {
        final Role admin = this.roleDAO.getAdminRole();
        final Set<UserRole> userRoles = this.userRoleDAO.findAllByRoleId(admin.getId());
        final Set<String> usersIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());
        return usersIds.size();
    }

    public void deleteById(final String id) {
        this.userRoleDAO.deleteByUserId(id);
        this.userDAO.deleteById(id);
    }

    public void insertUser(final UserPrincipal user) {
        this.userDAO.insert(user);
        this.userRoleDAO.insertRolesForUserId(user
                .getRoles()
                .stream()
                .map(role -> new UserRole(user.getUpdatedBy(), user.getId(), role.getId()))
                .collect(Collectors.toSet())
        );
    }
}
