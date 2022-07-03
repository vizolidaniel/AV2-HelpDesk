package br.com.daniel.model.decorator;

import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.domain.ServiceRequestStatus;
import br.com.daniel.security.domain.UserPrincipal;

public class ServiceRequestWithUsersData extends ServiceRequest {
    protected final UserPrincipal createdBy;
    protected final UserPrincipal analyzedBy;

    public ServiceRequestWithUsersData(
            final ServiceRequest request,
            final UserPrincipal createdBy,
            final UserPrincipal analyzedBy
    ) {
        super(
                request.getId(),
                request.getCreatedAt(),
                request.getCreatedBy(),
                request.getUpdatedAt(),
                request.getUpdatedBy(),
                request.getDescription(),
                request.getStatus(),
                request.getAnalyzedBy()
        );

        this.createdBy = createdBy;
        this.analyzedBy = analyzedBy;
    }

    public String getCreatedByAsString() {
        return this.createdBy.getName();
    }

    public String getCreatedAtAsString() {
        return this.getCreatedAt().toString();
    }

    public String getAnalyzedByAsString() {
        return this.analyzedBy.getName();
    }

    public boolean gotToAnalysis() {
        return this.getStatus() == ServiceRequestStatus.IN_ANALYSIS;
    }

    public String getShortDescription() {
        if (this.getDescription().length() > 10)
            return this.getDescription().substring(0, 9).concat("...");

        return this.getDescription();
    }

    public String getReadableId() {
        return this.getId().split("-")[0].toUpperCase();
    }
}
