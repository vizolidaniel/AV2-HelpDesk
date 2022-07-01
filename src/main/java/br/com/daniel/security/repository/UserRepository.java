package br.com.daniel.security.repository;

import br.com.daniel.exception.UserUpdateException;
import br.com.daniel.model.Response;
import br.com.daniel.security.domain.User;
import br.com.daniel.security.repository.statements.UserRepositoryStatements;
import br.com.daniel.security.repository.statements.UserRoleRepositoryStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(final String email) {
        final List<User> results = this.jdbcTemplate.query(UserRepositoryStatements.SELECT_BY_EMAIL, ps -> ps.setString(1, email), (rs, rowNum) -> new User(rs.getString("id"), rs.getDate("created_at"), rs.getString("updated_by"), rs.getDate("created_at"), rs.getString("created_by"), rs.getString("name"), rs.getString("email"), rs.getString("password")));

        if (results.isEmpty()) return Optional.empty();
        return Optional.ofNullable(results.get(0));
    }

    public Optional<User> findById(final String id) {
        final List<User> results = this.jdbcTemplate.query(UserRepositoryStatements.SELECT_BY_ID, ps -> ps.setString(1, id), (rs, rowNum) -> new User(rs.getString("id"), rs.getDate("created_at"), rs.getString("updated_by"), rs.getDate("created_at"), rs.getString("created_by"), rs.getString("name"), rs.getString("email"), rs.getString("password")));

        if (results.isEmpty()) return Optional.empty();
        return Optional.ofNullable(results.get(0));
    }

    public Response<User> findAll(final int page, final int size) {
        final List<User> results = this.jdbcTemplate.query(UserRepositoryStatements.PAGINATE_ALL, ps -> {
            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);
        }, (rs, rowNum) -> new User(rs.getString("id"), rs.getDate("created_at"), rs.getString("updated_by"), rs.getDate("created_at"), rs.getString("created_by"), rs.getString("name"), rs.getString("email"), rs.getString("password")));

        final long total = this.jdbcTemplate.query(UserRepositoryStatements.COUNT_ALL, (rs, rowNum) -> rs.getLong(1)).stream().findFirst().orElse(0L);

        return new Response<User>().builder().results(results).page(page).size(size).total(total).build();
    }

    public void update(User user) {
        final Map<String, Object> updatingUser = new HashMap<>();
        updatingUser.put("name", user.getName());
        updatingUser.put("email", user.getEmail());
        updatingUser.put("password", user.getPassword());
        updatingUser.put("updated_by", user.getUpdatedBy());
        updatingUser.put("updated_at", user.getUpdatedAt());

        final List<String> keys = new ArrayList<>(updatingUser.keySet());

        final String updatePattern = keys
                .stream()
                .map(key -> String.format("%s=?", key))
                .collect(Collectors.joining(","));

        final String sql = String.format(UserRepositoryStatements.UPDATE, updatePattern);

        try {
            final int updatedRows = this.jdbcTemplate.update(sql, ps -> {
                for (int i = 0; i < updatingUser.size(); i++) {
                    String key = keys.get(i);
                    Object value = updatingUser.get(key);
                    if (value instanceof Date)
                        ps.setTimestamp(
                                i + 1,
                                Timestamp.valueOf(LocalDateTime.ofInstant(
                                        ((Date) value).toInstant(),
                                        ZoneId.systemDefault())
                                )
                        );
                    else
                        ps.setString(i + 1, (String) value);
                }
                ps.setString(updatingUser.size() + 1, user.getId());
            });
            if (updatedRows < 1) throw new UserUpdateException("Nenhuma informação a atualizar", "/users");
        } catch (Exception ex) {
            throw new UserUpdateException(ex.getMessage(), "/users");
        }
    }

    public void deleteById(final String id) {
        try {
            final int updatedRows = this.jdbcTemplate.update(
                    UserRepositoryStatements.DELETE_BY_ID,
                    ps -> ps.setString(1, id)
            );
            if (updatedRows < 1) throw new UserUpdateException("Nenhuma informação de usuário a deletar", "/users");
        } catch (Exception ex) {
            throw new UserUpdateException(ex.getMessage(), "/users");
        }
    }

    public void insert(User user) {
        final Map<String, Object> insertingUser = new HashMap<>();
        insertingUser.put("id", user.getId());
        insertingUser.put("name", user.getName());
        insertingUser.put("email", user.getEmail());
        insertingUser.put("password", user.getPassword());
        insertingUser.put("updated_by", user.getUpdatedBy());
        insertingUser.put("updated_at", user.getUpdatedAt());
        insertingUser.put("created_by", user.getCreatedBy());
        insertingUser.put("created_at", user.getCreatedAt());

        final List<String> keys = new ArrayList<>(insertingUser.keySet());

        final String insertPattern = String.join(",", keys);
        final String valuesPattern = keys.stream().map(k -> "?").collect(Collectors.joining(","));

        final String sql = String.format(UserRepositoryStatements.INSERT_NEW, insertPattern, valuesPattern);

        try {
            final int updatedRows = this.jdbcTemplate.update(sql, ps -> {
                for (int i = 0; i < insertingUser.size(); i++) {
                    String key = keys.get(i);
                    Object value = insertingUser.get(key);
                    if (value instanceof Date)
                        ps.setTimestamp(
                                i + 1,
                                Timestamp.valueOf(LocalDateTime.ofInstant(
                                        ((Date) value).toInstant(),
                                        ZoneId.systemDefault())
                                )
                        );
                    else
                        ps.setString(i + 1, (String) value);
                }
            });
            if (updatedRows < 1) throw new UserUpdateException("Usuário não foi criado", "/users");
        } catch (Exception ex) {
            throw new UserUpdateException(ex.getMessage(), "/users");
        }
    }
}
