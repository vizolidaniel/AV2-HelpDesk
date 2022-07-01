package br.com.daniel.security.repository.statements;

public interface UserRepositoryStatements {
    String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email=?";
}
