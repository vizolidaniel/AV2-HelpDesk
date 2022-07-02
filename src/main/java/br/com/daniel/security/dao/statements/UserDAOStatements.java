package br.com.daniel.security.dao.statements;

public interface UserDAOStatements {
    String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    String SELECT_BY_ID = "SELECT * FROM users WHERE id=?";
    String PAGINATE_ALL = "SELECT * FROM users LIMIT ? OFFSET ?";
    String COUNT_ALL = "SELECT COUNT(1) FROM users";
    String INSERT_NEW = "INSERT INTO users(%s) VALUES(%s)";
    String UPDATE = "UPDATE users SET %s WHERE id=?";
    String DELETE_BY_ID = "DELETE FROM users WHERE id=?";
}
