package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Unsecured
@RequestMapping("/error")
public class ErrorController {

    @GetMapping
    public String error() {
        return "error";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
};