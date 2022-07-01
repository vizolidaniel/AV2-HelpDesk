package br.com.daniel.security.controller;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.exception.CanNotSelfDeleteException;
import br.com.daniel.exception.MissingParamException;
import br.com.daniel.model.Response;
import br.com.daniel.security.domain.Role;
import br.com.daniel.security.domain.User;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.service.RoleService;
import br.com.daniel.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@Authorized(roles = "ADMIN")
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public UserController(final UserService service, final RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping
    public String home(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            final Model model
    ) throws IOException {
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        final Response<UserPrincipal> results = this.service.paginateUsers(page, size);

        model.addAttribute("users", results.getResults());
        model.addAttribute("hasNextPage", results.hasNext());
        model.addAttribute("hasPreviousPage", results.hasPrevious());
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("previousPage", page - 1);

        return "users/index";
    }

    @GetMapping("/update/{id}")
    public String edit(@PathVariable(name = "id") final String id, final Model model) {
        final UserPrincipal updatingUser = this.service.findUserById(id);
        final List<Role> roles = new ArrayList<>(this.roleService.findAllRoles().getResults());

        model.addAttribute("roles", roles);
        model.addAttribute("updatingUser", updatingUser);

        return "users/edit";
    }

    @PostMapping("/update")
    public void update(final HttpServletRequest req, final HttpServletResponse res, final HttpSession session) throws IOException {
        final Map<String, String[]> params = req.getParameterMap();
        final AtomicReference<String> idRef = new AtomicReference<>(null);
        final AtomicReference<String> nameRef = new AtomicReference<>(null);
        final AtomicReference<String> emailRef = new AtomicReference<>(null);
        final AtomicReference<String> passwordRef = new AtomicReference<>(null);
        final AtomicReference<Set<Role>> rolesRef = new AtomicReference<>(new HashSet<>());

        params.forEach((key, values) -> {
            switch (key) {
                case "id":
                    idRef.set(values[0]);
                    break;
                case "nome":
                    nameRef.set(values[0]);
                    break;
                case "email":
                    emailRef.set(values[0]);
                    break;
                case "senha":
                    passwordRef.set(values[0]);
                    break;
                case "roles":
                    final Set<String> rolesIds = Arrays.stream(values).collect(Collectors.toSet());
                    final Set<Role> gotRoles = this.roleService.findRolesByIds(rolesIds);
                    rolesRef.set(gotRoles);
                    break;
            }
        });

        final Set<String> missing = new HashSet<>();

        final String id = idRef.get();
        if (!StringUtils.hasText(id)) missing.add("id");

        final String name = nameRef.get();
        if (!StringUtils.hasText(name)) missing.add("nome");

        final String email = emailRef.get();
        if (!StringUtils.hasText(email)) missing.add("e-mail");

        final String password = passwordRef.get();
        if (!StringUtils.hasText(password)) missing.add("senha");

        final Set<Role> roles = rolesRef.get();
        if (roles.isEmpty()) missing.add("permissões");

        if (!missing.isEmpty()) throw new MissingParamException(missing, String.format("/users/update/%s", id));

        final UserPrincipal loggedUser = (UserPrincipal) session.getAttribute("principal");
        final UserPrincipal user = this.service.findUserById(id);

        final UserPrincipal updatingUser = new UserPrincipal(
                new User(
                        id,
                        user.getCreatedAt(),
                        user.getCreatedBy(),
                        new Date(),
                        loggedUser.getEmail(),
                        name,
                        email,
                        password
                ),
                roles
        );

        this.service.update(updatingUser);

        session.setAttribute("message", "Usuário Atualizado com Sucesso!");

        res.sendRedirect("/users");
    }

    @GetMapping("/delete/{id}")
    public void delete(
            @PathVariable(name = "id") final String id,
            HttpSession session,
            HttpServletResponse response
    ) throws IOException {
        final UserPrincipal loggedUser = (UserPrincipal) session.getAttribute("principal");
        if (loggedUser.getId().equals(id)) throw new CanNotSelfDeleteException();

        this.service.deleteUser(id);

        session.setAttribute("message", "Usuário removido!");
        response.sendRedirect("/users");
    }

    @GetMapping("/create")
    public String create(final Model model) {
        final List<Role> roles = new ArrayList<>(this.roleService.findAllRoles().getResults());

        model.addAttribute("roles", roles);

        return "users/create";
    }

    @PostMapping("/create")
    public void create(final HttpServletRequest req, final HttpServletResponse res, final HttpSession session) throws IOException {
        final Map<String, String[]> params = req.getParameterMap();
        final AtomicReference<String> nameRef = new AtomicReference<>(null);
        final AtomicReference<String> emailRef = new AtomicReference<>(null);
        final AtomicReference<String> passwordRef = new AtomicReference<>(null);
        final AtomicReference<Set<Role>> rolesRef = new AtomicReference<>(new HashSet<>());

        params.forEach((key, values) -> {
            switch (key) {
                case "nome":
                    nameRef.set(values[0]);
                    break;
                case "email":
                    emailRef.set(values[0]);
                    break;
                case "senha":
                    passwordRef.set(values[0]);
                    break;
                case "roles":
                    final Set<String> rolesIds = Arrays.stream(values).collect(Collectors.toSet());
                    final Set<Role> gotRoles = this.roleService.findRolesByIds(rolesIds);
                    rolesRef.set(gotRoles);
                    break;
            }
        });

        final Set<String> missing = new HashSet<>();

        final String name = nameRef.get();
        if (!StringUtils.hasText(name)) missing.add("nome");

        final String email = emailRef.get();
        if (!StringUtils.hasText(email)) missing.add("e-mail");

        final String password = passwordRef.get();
        if (!StringUtils.hasText(password)) missing.add("senha");

        final Set<Role> roles = rolesRef.get();
        if (roles.isEmpty()) missing.add("permissões");

        if (!missing.isEmpty()) throw new MissingParamException(missing, "/users/create");

        final UserPrincipal loggedUser = (UserPrincipal) session.getAttribute("principal");

        final UserPrincipal creatingUser = new UserPrincipal(
                new User(
                        loggedUser.getEmail(),
                        name,
                        email,
                        password
                ),
                roles
        );

        this.service.create(creatingUser);

        session.setAttribute("message", "Usuário Criado com Sucesso!");

        res.sendRedirect("/users");
    }
}
