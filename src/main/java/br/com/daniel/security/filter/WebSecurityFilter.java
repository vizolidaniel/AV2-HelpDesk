package br.com.daniel.security.filter;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.annotations.Unsecured;
import br.com.daniel.security.domain.Role;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private static UserPrincipal principal() {
        final Object principal = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getSession()
                .getAttribute("principal");
        if (principal != null) return (UserPrincipal) principal;
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod)
            return this.handle(request, response, (HandlerMethod) handler);
        return true;
    }

    private boolean handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws IOException {
        if (isUnsecured(handler)) {
            log.info(String.format("url sem protecao: %s", request.getRequestURI()));
            return true;
        }

        final UserPrincipal principal = principal();
        if (principal != null && this.principalStillValid(principal)) {
            log.info(String.format("usuario logado: %s", principal.getEmail()));

            if (isUnderAuthorization(handler)) {
                final Set<String> authorizedRoles = authorizedRoles(handler);
                log.info(String.format("validando permissoes de usuario: %s", join(",", authorizedRoles)));
                log.info(String.format(
                        "permissoes do usuario: %s",
                        principal.getRoles().stream().map(Role::getRole).collect(Collectors.joining(","))
                ));

                if (principal.getRoles().stream().map(Role::getRole).collect(toSet()).containsAll(authorizedRoles)) {
                    log.info("o usuario tem todas as permissoes necessarias");
                    return true;
                }

                log.info("o usuario nao tem todas as permissoes necessarias");

                response.sendRedirect("/error/forbidden");
                return false;
            }

            return true;
        }

        log.info("deslogado, redirecionando para login");

        response.sendRedirect("/login");
        return false;
    }

    private boolean principalStillValid(final UserPrincipal principal) {
        return this.userService.userExists(principal);
    }
}
