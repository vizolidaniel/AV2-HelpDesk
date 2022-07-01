package br.com.daniel.controller;

import br.com.daniel.annotations.Authorized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Authorized(roles = "SERVICE_DESK")
@RequestMapping("/service-desk")
public class ServiceDeskController {

    @GetMapping
    public String home() {
        return "service-desk";
    }
}
