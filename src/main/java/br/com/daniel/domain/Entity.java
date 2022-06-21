package br.com.daniel.domain;

import java.util.Date;

public abstract class Entity {
    private final String id;
    private final Date createdAt;
    private Date updatedAt;
    private final String createdBy;
    private String updatedBy;

    public Entity(final String id, final Date createdAt, final String createdBy) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
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
}
