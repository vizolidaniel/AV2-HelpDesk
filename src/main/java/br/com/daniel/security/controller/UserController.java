package br.com.daniel.security.controller;

import br.com.daniel.annotations.Authorized;
import br.com.daniel.exception.CanNotSelfDeleteException;
import br.com.daniel.model.Response;
import br.com.daniel.security.domain.Role;
import br.com.daniel.security.domain.User;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.model.dto.UserDTO;
import br.com.daniel.security.model.validation.UserCreateValidation;
import br.com.daniel.security.model.validation.UserUpdateValidation;
import br.com.daniel.security.service.RoleService;
import br.com.daniel.security.service.UserService;
import br.com.daniel.utils.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@Authorized(roles = "ADMIN")
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final RoleService roleService;

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
    public String update(final HttpSession session, @ModelAttribute final UserDTO dto) throws IOException {
        UserUpdateValidation.validate(dto);

        final UserPrincipal loggedUser = Principal.extract();
        final UserPrincipal user = this.service.findUserById(dto.getId());

        final Set<Role> gotRoles = this.roleService.findRolesByIds(dto.getRoles());

        final UserPrincipal updatingUser = new UserPrincipal(
                new User(
                        dto.getId(),
                        user.getCreatedAt(),
                        user.getCreatedBy(),
                        new Date(),
                        loggedUser.getId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPassword()
                ),
                gotRoles
        );

        this.service.update(updatingUser);

        session.setAttribute("message", "Usuário Atualizado com Sucesso!");

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final String id, final HttpSession session) {
        final UserPrincipal loggedUser = Principal.extract();
        if (loggedUser.getId().equals(id)) throw new CanNotSelfDeleteException();

        this.service.deleteUser(id);

        session.setAttribute("message", "Usuário removido!");
        return "redirect:/users";
    }

    @GetMapping("/create")
    public String create(final Model model) {
        final List<Role> roles = new ArrayList<>(this.roleService.findAllRoles().getResults());

        model.addAttribute("roles", roles);

        return "users/create";
    }

    @PostMapping("/create")
    public String create(final HttpSession session, @ModelAttribute final UserDTO dto) throws IOException {
        UserCreateValidation.validate(dto);

        final UserPrincipal loggedUser = Principal.extract();

        final Set<Role> gotRoles = this.roleService.findRolesByIds(dto.getRoles());

        final UserPrincipal creatingUser = new UserPrincipal(
                new User(
                        loggedUser.getId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPassword()
                ),
                gotRoles
        );

        this.service.create(creatingUser);

        session.setAttribute("message", "Usuário Criado com Sucesso!");

        return "redirect:/users";
    }
}
