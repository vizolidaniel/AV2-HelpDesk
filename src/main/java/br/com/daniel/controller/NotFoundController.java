package br.com.daniel.controller;

import br.com.daniel.annotations.Unsecured;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Unsecured
@Controller
public class NotFoundController {
    private static final Log log = LogFactory.getLog(NotFoundController.class);

    @GetMapping("/**")
    public void handle(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        log.info(String.format("pagina nao encontrada: %s", req.getRequestURI()));

        res.sendRedirect("/");
    }
}
