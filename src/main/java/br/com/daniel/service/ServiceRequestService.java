package br.com.daniel.service;

import br.com.daniel.dao.ServiceRequestCommentDAO;
import br.com.daniel.dao.ServiceRequestDAO;
import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.domain.ServiceRequestComment;
import br.com.daniel.domain.ServiceRequestStatus;
import br.com.daniel.exception.ForbiddenException;
import br.com.daniel.exception.RequestNotFoundException;
import br.com.daniel.model.Response;
import br.com.daniel.model.decorator.ServiceRequestCommentWithUsersData;
import br.com.daniel.model.decorator.ServiceRequestWithUsersData;
import br.com.daniel.model.decorator.ServiceRequestWithUsersDataAndComments;
import br.com.daniel.model.dto.ServiceRequestCommentDTO;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.permissions.ViewRoles;
import br.com.daniel.security.service.UserService;
import br.com.daniel.utils.Principal;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ServiceRequestService {
    private final ServiceRequestDAO serviceRequestDAO;
    private final UserService userService;

    private final ServiceRequestCommentDAO serviceRequestCommentDAO;

    public ServiceRequestService(
            final ServiceRequestDAO serviceRequestDAO,
            final UserService userService,
            final ServiceRequestCommentDAO serviceRequestCommentDAO
    ) {
        this.serviceRequestDAO = serviceRequestDAO;
        this.userService = userService;
        this.serviceRequestCommentDAO = serviceRequestCommentDAO;
    }

    public void create(final ServiceRequest serviceRequest) {
        this.serviceRequestDAO.insert(serviceRequest);
    }

    public Response<ServiceRequestWithUsersData> findAll(
            final int page,
            final int size,
            final String status
    ) {
        if (ViewRoles.isAdmin())
            return mapResults(this.serviceRequestDAO.findAll(page, size, status));

        UserPrincipal loggedUser = Principal.extract();

        if (ViewRoles.isHelper())
            return mapResults(this.serviceRequestDAO.findAllAnalyzedBy(page, size, status, loggedUser.getId()));

        return mapResults(this.serviceRequestDAO.findAllFromUser(page, size, status, loggedUser.getId()));
    }

    public ServiceRequestWithUsersDataAndComments findById(final String id) {
        return this.serviceRequestDAO
                .findById(id)
                .map(req -> new ServiceRequestWithUsersDataAndComments(new ServiceRequestWithUsersData(
                        req,
                        this.userService.findUserById(req.getCreatedBy()),
                        this.userService.findUserById(req.getAnalyzedBy())),
                        this.serviceRequestCommentDAO.findAllByServiceRequestId(req.getId())
                                .stream()
                                .map(comment -> new ServiceRequestCommentWithUsersData(
                                        comment,
                                        this.userService.findUserById(comment.getCreatedBy()).getName()
                                ))
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    public void addComment(final ServiceRequestCommentDTO dto, final String serviceRequestId, final boolean close) {
        final ServiceRequestWithUsersData request = this.findById(serviceRequestId);
        if (request.getStatus() == ServiceRequestStatus.CLOSED)
            throw new ForbiddenException(
                    "Chamado já encerrado!",
                    String.format("/requests/%s", serviceRequestId)
            );

        final UserPrincipal loggedPrincipal = Principal.extract();

        if (request.getStatus() == ServiceRequestStatus.OPEN && ViewRoles.isHelper()) {
            request.setStatus(ServiceRequestStatus.IN_ANALYSIS);
            request.setAnalyzedBy(loggedPrincipal.getId());
            this.serviceRequestDAO.update(request);
        }

        if (close
                && !loggedPrincipal.getId().equals(request.getCreatedBy())
                && !loggedPrincipal.getId().equals(request.getAnalyzedBy()))
            throw new ForbiddenException(
                    "Você não está relacionado a este chamado!",
                    String.format("/requests/%s", serviceRequestId)
            );

        final ServiceRequestComment comment = new ServiceRequestComment(
                loggedPrincipal.getId(),
                dto.getComment(),
                serviceRequestId
        );

        this.serviceRequestCommentDAO.insert(comment);
        if (close) {
            request.setStatus(ServiceRequestStatus.CLOSED);
            this.serviceRequestDAO.update(request);
        }
    }

    private Response<ServiceRequestWithUsersData> mapResults(final Response<ServiceRequest> result) {
        return result.map(req -> new ServiceRequestWithUsersData(
                req,
                this.userService.findUserById(req.getCreatedBy()),
                this.userService.findUserById(req.getAnalyzedBy())
        ), Collectors.toList());
    }
}
