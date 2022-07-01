package br.com.daniel.security.repository.statements;

public interface RoleRepositoryStatements {
    String SELECT_BY_ID = "SELECT * FROM roles WHERE id IN (%s)";
}
