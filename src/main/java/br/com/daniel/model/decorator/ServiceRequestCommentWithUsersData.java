package br.com.daniel.model.decorator;

import br.com.daniel.domain.ServiceRequestComment;

public class ServiceRequestCommentWithUsersData extends ServiceRequestComment {
    private final String createdByName;

    public ServiceRequestCommentWithUsersData(final ServiceRequestComment comment, String createdByName) {
        super(
                comment.getId(),
                comment.getCreatedAt(),
                comment.getCreatedBy(),
                comment.getUpdatedAt(),
                comment.getUpdatedBy(),
                comment.getComment(),
                comment.getServiceRequestsId()
        );

        this.createdByName = createdByName;
    }

    public String getCreatedByName() {
        return this.createdByName;
    }

    public String getCreatedAtAsString() {
        return this.getCreatedAt().toString();
    }
}
