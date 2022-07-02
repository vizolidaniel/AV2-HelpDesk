package br.com.daniel.controller;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.model.dto.ServiceRequestDTO;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.utils.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static br.com.daniel.model.mapper.ServiceRequestMapper.map;
import static br.com.daniel.model.validation.ServiceRequestValidation.validate;

@Controller
@RequestMapping("/requests")
public class ServiceDeskController {

    @GetMapping
    @Authorized(roles = "#canViewRequests")
    public String home() {
        return "service-desk/index";
    }

    @GetMapping("/my")
    @Authorized(roles = "#canViewSelfRequests")
    public String requests() {
        return "service-desk/my";
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

        session.setAttribute("message", "Chamado registrado com sucesso");
        return "redirect:/requests";
    }
}
