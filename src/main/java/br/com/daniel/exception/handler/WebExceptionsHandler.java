package br.com.daniel.exception.handler;

import br.com.daniel.exception.WebException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ControllerAdvice
public class WebExceptionsHandler {
    private static final Log log = LogFactory.getLog(WebExceptionsHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingParams(
            final MissingServletRequestParameterException ex,
            final HttpSession session,
            final HttpServletRequest req,
            final HttpServletResponse res
    ) throws IOException, ServletException {
        session.setAttribute("message", String.format("preencha os campos: %s", ex.getParameterName()));
        res.sendRedirect(req.getRequestURI());
    }

    @ExceptionHandler(WebException.class)
    public void handleWebException(
            final WebException ex,
            final HttpSession session,
            final HttpServletResponse res
    ) throws IOException, ServletException {
        session.setAttribute("message", ex.getMessage());
        res.sendRedirect(ex.getRedirect());
    }

    @ExceptionHandler(Exception.class)
    public void handleUncaughtException(
            final Exception ex,
            final HttpSession session,
            final HttpServletResponse res
    ) throws IOException {
        log.error("General Internal Error", ex);
        session.setAttribute("message", ex.getMessage());
        res.sendRedirect("/error");
    }
}
