package br.com.daniel.security.controller;

import br.com.daniel.annotations.Authorized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Authorized(roles = "ADMIN")
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String home() {
        return "users";
    }
}
