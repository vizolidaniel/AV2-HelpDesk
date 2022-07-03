package br.com.daniel.model.decorator;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestWithUsersDataAndComments extends ServiceRequestWithUsersData {
    private final List<ServiceRequestCommentWithUsersData> comments;

    public ServiceRequestWithUsersDataAndComments(
            final ServiceRequestWithUsersData request,
            final List<ServiceRequestCommentWithUsersData> comments
    ) {
        super(request, request.createdBy, request.analyzedBy);

        this.comments = comments;
    }

    public ServiceRequestWithUsersDataAndComments(final ServiceRequestWithUsersData request) {
        this(request, new ArrayList<>());
    }

    public List<ServiceRequestCommentWithUsersData> getComments() {
        return this.comments;
    }
}
