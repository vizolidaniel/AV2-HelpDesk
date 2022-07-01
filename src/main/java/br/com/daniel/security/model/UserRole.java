package br.com.daniel.security.model;

import br.com.daniel.domain.Entity;

import java.util.Date;
import java.util.Objects;

public class UserRole extends Entity {
    private final String userId;
    private final String roleId;

    public UserRole(
            final String id,
            final Date createdAt,
            final String createdBy,
            final Date updatedAt,
            final String updatedBy,
            final String userId,
            final String roleId
    ) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole(final String createdBy, final String userId, final String roleId) {
        super(createdBy);
        this.userId = userId;
        this.roleId = roleId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserRole userRole = (UserRole) o;

        if (!Objects.equals(userId, userRole.userId)) return false;
        return Objects.equals(roleId, userRole.roleId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
