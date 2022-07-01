package br.com.daniel.security.domain;

import br.com.daniel.domain.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal extends User {
    private final Set<Role> roles;

    public UserPrincipal(final User user, final Set<Role> roles) {
        super(
                user.getId(),
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getUpdatedAt(),
                user.getUpdatedBy(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public Set<String> listRoles() {
        return this.roles.stream().map(Role::getRole).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserPrincipal that = (UserPrincipal) o;

        return this.roles.equals(that.roles);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.roles.hashCode();
        return result;
    }
}
