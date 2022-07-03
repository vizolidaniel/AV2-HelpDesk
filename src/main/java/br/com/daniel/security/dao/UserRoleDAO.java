package br.com.daniel.security.dao;

import br.com.daniel.exception.UpdateException;
import br.com.daniel.security.dao.statements.UserRoleDAOStatements;
import br.com.daniel.security.model.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRoleDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserRoleDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<UserRole> findUserRolesByUserId(final String userId) {
        final List<UserRole> results = this.jdbcTemplate.query(
                UserRoleDAOStatements.SELECT_BY_USER_ID,
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

    public void deleteByUserId(final String userId) {
        try {
            final int updatedRows = this.jdbcTemplate.update(
                    UserRoleDAOStatements.DELETE_BY_USER_ID,
                    ps -> ps.setString(1, userId)
            );
            if (updatedRows < 1) throw new UpdateException("Nenhuma informação de role a atualizar", "/users");
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage(), "/users");
        }
    }

    public void insertRolesForUserId(final Set<UserRole> roles) {
        roles.forEach(role -> {
            final Map<String, Object> insertingRole = new HashMap<>();
            insertingRole.put("id", role.getId());
            insertingRole.put("role_id", role.getRoleId());
            insertingRole.put("user_id", role.getUserId());
            insertingRole.put("updated_by", role.getUpdatedBy());
            insertingRole.put("updated_at", role.getUpdatedAt());
            insertingRole.put("created_by", role.getCreatedBy());
            insertingRole.put("created_at", role.getCreatedAt());

            final List<String> keys = new ArrayList<>(insertingRole.keySet());

            final String keyPattern = String.join(",", keys);
            final String valuesPattern = keys.stream().map(k -> "?").collect(Collectors.joining(","));

            final String sql = String.format(
                    UserRoleDAOStatements.INSERT_NEW,
                    keyPattern,
                    valuesPattern
            );
            try {
                final int updatedRows = this.jdbcTemplate.update(sql, ps -> {
                    for (int i = 0; i < keys.size(); i++) {
                        String key = keys.get(i);
                        Object value = insertingRole.get(key);
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
                if (updatedRows < 1) throw new UpdateException("Nenhuma informação de permissão a iriar", "/users");
            } catch (Exception ex) {
                throw new UpdateException(ex.getMessage(), "/users");
            }
        });
    }

    public Set<UserRole> findAllByRoleId(final String roleId) {
        final List<UserRole> results = this.jdbcTemplate.query(
                UserRoleDAOStatements.SELECT_BY_ROLE_ID,
                ps -> ps.setString(1, roleId),
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
