package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import br.com.daniel.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@Unsecured
@RequestMapping("/login")
public class LoginController {
    private final UserService service;

    @Autowired
    public LoginController(final UserService service) {
        this.service = service;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public String login(
            @RequestParam(name = "email") final String email,
            @RequestParam(name = "senha") final String password,
            HttpSession session
    ) throws IOException {
        this.service.login(email, password);

        if (session.getAttribute("principal") != null)
            return "index";

        return "login";
    }
}