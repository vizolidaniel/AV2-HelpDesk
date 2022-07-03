package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Unsecured
@Controller
@RequestMapping("/resources")
public class StaticResourcesController {

    @GetMapping("/**")
    public String get(final HttpServletRequest req) {
        return String.format("resources/%s", req.getRequestURI().replace("/resources/", ""));
    }

}
