package br.com.daniel.domain;

import java.util.Date;

public class ServiceRequest extends Entity {
    private String description;
    private ServiceRequestStatus status;

    public ServiceRequest(
            final String id,
            final Date createdAt,
            final String createdBy,
            final Date updatedAt,
            final String updatedBy,
            final String description,
            final ServiceRequestStatus status
    ) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.description = description;
        this.status = status;
    }

    public ServiceRequest(final String createdBy, final String description) {
        super(createdBy);
        this.description = description;
        this.status = ServiceRequestStatus.OPEN;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ServiceRequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(final ServiceRequestStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ServiceRequest that = (ServiceRequest) o;

        if (!description.equals(that.description)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
