package br.com.daniel.security.domain;

import br.com.daniel.domain.Entity;

import java.util.Date;

public class Role extends Entity {
    private final String role;

    public Role(
            final String id,
            final Date createdAt,
            final String createdBy,
            final Date updatedAt,
            final String updatedBy,
            final String role
    ) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.role = role;
    }

    public Role(final String id, final Date createdAt, final String createdBy, final String role) {
        super(id, createdAt, createdBy);
        this.role = role;
    }

    public Role(final String createdBy, final String role) {
        super(createdBy);
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role that = (Role) o;

        return this.role.equals(that.role);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.role.hashCode();
        return result;
    }
}
