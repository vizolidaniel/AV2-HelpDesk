package br.com.daniel.controller;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.domain.ServiceRequestStatus;
import br.com.daniel.exception.ForbiddenException;
import br.com.daniel.model.Response;
import br.com.daniel.model.decorator.ServiceRequestCommentWithUsersData;
import br.com.daniel.model.decorator.ServiceRequestWithUsersData;
import br.com.daniel.model.decorator.ServiceRequestWithUsersDataAndComments;
import br.com.daniel.model.dto.ServiceRequestCommentDTO;
import br.com.daniel.model.dto.ServiceRequestDTO;
import br.com.daniel.model.validation.ServiceRequestCommentValidation;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.permissions.ViewRoles;
import br.com.daniel.service.ServiceRequestService;
import br.com.daniel.utils.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static br.com.daniel.model.mapper.ServiceRequestMapper.map;
import static br.com.daniel.model.validation.ServiceRequestValidation.validate;

@Controller
@Authorized(roles = "#canViewRequests")
@RequestMapping("/requests")
public class ServiceDeskController {
    private final ServiceRequestService service;

    public ServiceDeskController(final ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public String requests(
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size,
            @RequestParam(required = false, defaultValue = "", name = "status") String status,
            final Model model
    ) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (!StringUtils.hasText(status) && ViewRoles.isHelper()) status = "OPEN";

        final Response<ServiceRequestWithUsersData> result = this.service.findAll(page, size, status);

        final int thisPage = result.getPage();
        final int thisSize = result.getSize();
        final List<ServiceRequestWithUsersData> requests = new ArrayList<>(result.getResults());

        model.addAttribute("thisPage", thisPage);
        model.addAttribute("nextPage", result.hasNext() ? thisPage + 1 : thisPage);
        model.addAttribute("previousPage", result.hasPrevious() ? thisPage - 1 : thisPage);
        model.addAttribute("thisSize", thisSize);
        model.addAttribute("requests", requests);

        model.addAttribute("possibleStatus", ServiceRequestStatus.values());

        try {
            ServiceRequestStatus selectedStatus = ServiceRequestStatus.valueOf(status);
            model.addAttribute("selectedStatus", selectedStatus);
        } catch (Exception ex) {
            // do nothing
        }

        return "service-desk/index";
    }

    @GetMapping("/create")
    @Authorized(roles = "#canCreateRequests")
    public String create() {
        return "service-desk/create";
    }

    @PostMapping("/create")
    @Authorized(roles = "#canCreateRequests")
    public String create(final HttpSession session, @ModelAttribute final ServiceRequestDTO dto) throws IOException {
        validate(dto);

        UserPrincipal principal = Principal.extract();

        final ServiceRequest serviceRequest = map(dto, principal.getId());

        this.service.create(serviceRequest);

        session.setAttribute("message", "Chamado aberto com sucesso");
        return "redirect:/requests";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable(name = "id") final String id, final Model model) {
        final ServiceRequestWithUsersDataAndComments request = this.service.findById(id);
        UserPrincipal loggedUser = Principal.extract();
        if (!loggedUser.getId().equals(request.getCreatedBy()) && !ViewRoles.canManageRequests())
            throw new ForbiddenException("Você não pode acessar este recurso.");

        model.addAttribute("request", request);

        return "service-desk/update";
    }

    @PostMapping("/{id}/comment")
    public String addComment(
            @PathVariable(name = "id") final String id,
            @RequestParam(name = "close", required = false, defaultValue = "false") final boolean close,
            @ModelAttribute final ServiceRequestCommentDTO dto,
            final HttpSession session
    ) {
        ServiceRequestCommentValidation.validate(dto, id);

        this.service.addComment(dto, id, close);

        session.setAttribute("message", "Comentário adicionado!");

        return String.format("redirect:/requests/%s", id);
    }
}
