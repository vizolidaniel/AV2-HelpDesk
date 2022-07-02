package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import br.com.daniel.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GivingAccessHomeController {
    @Autowired
    UserService userService;

    @GetMapping
    @Unsecured
    public String home() {
        this.userService.login("admin", "YWRtaW4=");
        return "index";
    }

};