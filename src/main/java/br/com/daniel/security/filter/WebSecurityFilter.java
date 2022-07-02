package br.com.daniel.security.filter;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.annotations.Unsecured;
import br.com.daniel.exception.ForbiddenException;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.permissions.ViewRoles;
import br.com.daniel.security.service.UserService;
import br.com.daniel.utils.Principal;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.stream.Collectors.toSet;

@Component
public class WebSecurityFilter implements HandlerInterceptor {
    private static final Log log = LogFactory.getLog(WebSecurityFilter.class);
    private final UserService userService;

    public WebSecurityFilter(final UserService userService) {
        this.userService = userService;
    }

    private static boolean isUnsecured(final HandlerMethod handler) {
        final Unsecured unsecuredClass = handler.getBean().getClass().getAnnotation(Unsecured.class);
        if (unsecuredClass != null) return true;

        final Unsecured unsecuredMethod = handler.getMethodAnnotation(Unsecured.class);
        return unsecuredMethod != null;
    }

    private static boolean isUnderAuthorization(final HandlerMethod handler) {
        final Authorized authorizingClass = handler.getBean().getClass().getAnnotation(Authorized.class);
        if (authorizingClass != null) return true;

        final Authorized authorizingMethod = handler.getMethodAnnotation(Authorized.class);
        return authorizingMethod != null;
    }

    private static Set<String> authorizedRoles(final HandlerMethod handler) {
        final Authorized authorizingClass = handler.getBean().getClass().getAnnotation(Authorized.class);
        if (authorizingClass != null) return Arrays.stream(authorizingClass.roles()).collect(toSet());

        final Authorized authorizingMethod = handler.getMethodAnnotation(Authorized.class);
        if (authorizingMethod != null) return Arrays.stream(authorizingMethod.roles()).collect(toSet());
        return new HashSet<>();
    }

    private static void validatePermissions(HandlerMethod handler, HttpServletRequest req) {
        try {
            final Set<String> authorizedRoles = authorizedRoles(handler);

            final Set<String> authorizationMethods = authorizedRoles
                    .stream()
                    .filter(auth -> auth.startsWith("#"))
                    .map(auth -> auth.replace("#", ""))
                    .collect(toSet());

            if (!authorizationMethods.isEmpty()) {
                if (authorizationMethods.size() > 1 || authorizedRoles.size() > 1)
                    throw new RuntimeException(String.format(
                            "Só é permitido apenas um método de validação de permissão: %s",
                            req.getRequestURI()
                    ));
                Method authorizationMethod = ViewRoles.class.getDeclaredMethod(new ArrayList<>(authorizationMethods).get(0));
                boolean authorized = (boolean) authorizationMethod.invoke(ViewRoles.class);

                if (!authorized) throw new RuntimeException("Você não tem permissões para este recurso");

                return;
            }

            final Set<String> loggedUserRoles = Principal.extract().listRoles();

            log.info(String.format("Permissões do recurso: %s", join(",", authorizedRoles)));
            log.info(String.format("Permissões do usu´rio: %s", String.join(",", loggedUserRoles)));

            if (loggedUserRoles.containsAll(authorizedRoles)) {
                log.info("o usuario tem todas as permissoes necessarias");
                return;
            }

            log.info("o usuario nao tem todas as permissoes necessarias");
        } catch (Exception ex) {
            final String error = String.format("Erro ao tentar validar permissões: %s", ex.getMessage());
            log.error(error);
            throw new ForbiddenException(error);
        }
        throw new ForbiddenException("Você não tem permissões para este recurso");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) return this.handle(request, (HandlerMethod) handler);
        return true;
    }

    private boolean handle(HttpServletRequest request, HandlerMethod handler) {
        if (isUnsecured(handler)) {
            log.info(String.format("url sem protecao: %s", request.getRequestURI()));
            return true;
        }

        final UserPrincipal principal = Principal.extract();
        if (this.principalStillValid(principal)) {
            log.info(String.format("usuario logado: %s", principal.getEmail()));

            if (isUnderAuthorization(handler)) {
                validatePermissions(handler, request);
            }

            return true;
        }

        Principal.invalidate();
        log.info("deslogado, redirecionando para login");

        return false;
    }

    private boolean principalStillValid(final UserPrincipal principal) {
        return this.userService.userExists(principal);
    }
}
