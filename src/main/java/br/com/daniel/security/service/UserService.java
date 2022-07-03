package br.com.daniel.security.service;

import br.com.daniel.exception.TooLessAdminsException;
import br.com.daniel.exception.UserNotFoundException;
import br.com.daniel.exception.UserPrincipalException;
import br.com.daniel.model.Response;
import br.com.daniel.security.dao.UserPrincipalDAO;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.utils.Base64Utils;
import br.com.daniel.utils.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Service
public class UserService {
    private static final int THIRTY_DAYS = 30 * 24 * 3600;
    private final UserPrincipalDAO repository;

    public UserService(final UserPrincipalDAO repository) {
        this.repository = repository;
    }

    public void login(final String email, final String password) {
        final UserPrincipal user = this.repository
                .findByEmail(email)
                .orElseThrow(UserPrincipalException::new);

        final String decodedPassword = Base64Utils.decode(password);

        if (PasswordUtils.matches(decodedPassword, user.getPassword())) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getSession(true);
            session.setAttribute("principal", user);
            session.setMaxInactiveInterval(THIRTY_DAYS);
        } else
            throw new UserPrincipalException();
    }

    public UserPrincipal findUserById(final String id) {
        return this.repository.findById(id).orElseThrow(() -> new UserNotFoundException(id, "/users"));
    }

    public boolean userExists(final UserPrincipal principal) {
        final UserPrincipal user = this.repository
                .findByEmail(principal.getEmail())
                .orElseThrow(UserPrincipalException::new);

        return user != null;
    }

    public Response<UserPrincipal> paginateUsers(final int page, final int size) {
        return this.repository.paginateAll(page, size);
    }

    public void update(final UserPrincipal user) {
        user.setPassword(PasswordUtils.hash(Base64Utils.decode(user.getPassword())));
        validateUpdate(user);
        this.repository.updateUser(user);
    }

    public void deleteUser(final String id) {
        final UserPrincipal user = this.findUserById(id);
        user.getRoles()
                .stream()
                .filter(role -> role.getRole().equals("ADMIN"))
                .forEach(role -> user.getRoles().remove(role));

        validateUpdate(user);

        this.repository.deleteById(id);
    }

    public void create(final UserPrincipal user) {
        user.setPassword(PasswordUtils.hash(Base64Utils.decode(user.getPassword())));
        this.repository.insertUser(user);
    }

    private void validateUpdate(final UserPrincipal updatingUser) {
        final UserPrincipal user = this.findUserById(updatingUser.getId());
        final Set<String> userRoles = user.listRoles();
        final Set<String> updatingRoles = updatingUser.listRoles();

        if (!updatingRoles.contains("ADMIN") && userRoles.contains("ADMIN"))
            if (this.repository.countAdmins() < 2) throw new TooLessAdminsException();
    }
}
