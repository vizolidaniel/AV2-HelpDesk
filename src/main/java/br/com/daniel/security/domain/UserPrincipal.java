package br.com.daniel.security.domain;

import br.com.daniel.domain.User;

import java.util.Date;
import java.util.Set;

public class UserPrincipal extends User {
    private final Set<Role> roles;

    public UserPrincipal(final String id, final Date createdAt, final String createdBy, final Set<Role> roles) {
        super(id, createdAt, createdBy);
        this.roles = roles;
    }

    public UserPrincipal(String createdBy, final Set<Role> roles) {
        super(createdBy);
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return this.roles;
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
