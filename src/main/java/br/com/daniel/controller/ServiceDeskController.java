package br.com.daniel.controller;

import br.com.daniel.annotations.Authorized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service-desk")
public class ServiceDeskController {

    @GetMapping
    @Authorized(roles = "SERVICE_DESK")
    public String home() {
        return "service-desk/index";
    }

    @GetMapping("/requests")
    @Authorized(roles = "CLIENT")
    public String requests() {
        return "service-desk/requests";
    }
}
