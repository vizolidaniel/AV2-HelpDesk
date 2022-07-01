package br.com.daniel.security.repository;

import br.com.daniel.security.model.UserRole;
import br.com.daniel.security.repository.statements.UserRoleRepositoryStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserRoleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRoleRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<UserRole> findUserRolesByUserId(final String userId) {
        final List<UserRole> results = this.jdbcTemplate.query(
                UserRoleRepositoryStatements.SELECT_BY_USER_ID,
                ps -> ps.setString(1, userId),
                (rs, rowNum) -> new UserRole(
                        rs.getString("id"),
                        rs.getDate("created_at"),
                        rs.getString("created_by"),
                        rs.getDate("updated_at"),
                        rs.getString("updated_by"),
                        rs.getString("user_id"),
                        rs.getString("role_id")
                )
        );

        return new HashSet<>(results);
    }

}
