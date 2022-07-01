package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Unsecured
@Controller
public class NotFoundController {
    private static final Log log = LogFactory.getLog(NotFoundController.class);

    @GetMapping("/{page}")
    public String handle(@PathVariable(name = "page") final String page) {
        log.info(String.format("pagina nao encontrada: %s", page));

        return "forward:login";
    }
}
