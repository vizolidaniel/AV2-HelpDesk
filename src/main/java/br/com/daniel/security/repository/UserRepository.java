package br.com.daniel.security.repository;

import br.com.daniel.domain.User;
import br.com.daniel.security.repository.statements.UserRepositoryStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(final String email) {
        final List<User> results = this.jdbcTemplate.query(
                UserRepositoryStatements.SELECT_BY_EMAIL,
                ps -> ps.setString(1, email),
                (rs, rowNum) -> new User(
                        rs.getString("id"),
                        rs.getDate("created_at"),
                        rs.getString("updated_by"),
                        rs.getDate("created_at"),
                        rs.getString("created_by"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                )
        );

        if (results.isEmpty()) return Optional.empty();
        return Optional.ofNullable(results.get(0));
    }

}
