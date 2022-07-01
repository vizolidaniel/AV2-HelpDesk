package br.com.daniel.security.service;

import br.com.daniel.exception.UserPrincipalException;
import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.security.repository.UserPrincipalRepository;
import br.com.daniel.utils.Base64Utils;
import br.com.daniel.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private static final int THIRTY_MINUTES = 1800;
    private final UserPrincipalRepository repository;

    @Autowired
    public UserService(final UserPrincipalRepository repository) {
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
            session.setMaxInactiveInterval(THIRTY_MINUTES);
        }
        else
            throw new UserPrincipalException();
    }

    public boolean userExists(final UserPrincipal principal) {
        final UserPrincipal user = this.repository
                .findByEmail(principal.getEmail())
                .orElseThrow(UserPrincipalException::new);

        return user != null;
    }
}
