package br.com.daniel.domain;

import java.util.Date;
import java.util.UUID;

public abstract class Entity {
    private final String id;
    private final Date createdAt;
    private Date updatedAt;
    private final String createdBy;
    private String updatedBy;

    public Entity(
            final String id,
            final Date createdAt,
            final String createdBy,
            final Date updatedAt,
            final String updatedBy
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public Entity(final String id, final Date createdAt, final String createdBy) {
        this(id, createdAt, createdBy, createdAt, createdBy);
    }

    public Entity(final String createdBy) {
        this(UUID.randomUUID().toString(), new Date(), createdBy);
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return this.id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
