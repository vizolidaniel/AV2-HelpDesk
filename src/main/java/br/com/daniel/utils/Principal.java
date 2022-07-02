package br.com.daniel.utils;

import br.com.daniel.exception.UserPrincipalException;
import br.com.daniel.security.domain.UserPrincipal;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class Principal {
    private static final Log log = LogFactory.getLog(Principal.class);

    private Principal() {
    }

    public static UserPrincipal extract() {
        try {
            final HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getSession();

            Object loggedUserAttr = session.getAttribute("principal");
            if (loggedUserAttr != null) return ((UserPrincipal) loggedUserAttr);
        } catch (Exception ex) {
            log.error("No session or principal found");
        }

        throw new UserPrincipalException();
    }

    public static void invalidate() {
        try {
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getSession()
                    .invalidate();
            throw new UserPrincipalException();
        } catch (Exception ex) {
            log.error("No session or principal found");
        }

        throw new UserPrincipalException();
    }
}
