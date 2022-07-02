package br.com.daniel.security.dao.statements;

public interface UserRoleDAOStatements {
    String SELECT_BY_USER_ID = "SELECT * FROM users_roles WHERE user_id=?";
    String DELETE_BY_USER_ID = "DELETE FROM users_roles WHERE user_id=?";
    String INSERT_NEW = "INSERT INTO users_roles(%s) VALUES(%s)";
    String SELECT_BY_ROLE_ID = "SELECT * FROM users_roles WHERE role_id=?";
}
