package br.com.daniel.domain;

import java.util.Date;

public class ServiceRequestComment extends Entity {
    private final String comment;
    private final String serviceRequestsId;

    public ServiceRequestComment(final String createdBy, final String comment, final String serviceRequestsId) {
        super(createdBy);
        this.comment = comment;
        this.serviceRequestsId = serviceRequestsId;
    }

    public ServiceRequestComment(
            final String id,
            final Date createdAt,
            final String createdBy,
            final Date updatedAt,
            final String updatedBy,
            final String comment,
            final String serviceRequestsId
    ) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.comment = comment;
        this.serviceRequestsId = serviceRequestsId;
    }

    public String getComment() {
        return this.comment;
    }

    public String getServiceRequestsId() {
        return this.serviceRequestsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ServiceRequestComment that = (ServiceRequestComment) o;

        if (!comment.equals(that.comment)) return false;
        return serviceRequestsId.equals(that.serviceRequestsId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + comment.hashCode();
        result = 31 * result + serviceRequestsId.hashCode();
        return result;
    }
}
