package br.com.daniel.security.repository.statements;

public interface RoleRepositoryStatements {
    String SELECT_BY_ID = "SELECT * FROM roles WHERE id IN (%s)";
    String SELECT_ALL = "SELECT * FROM roles";
    String SELECT_ADMIN_ROLE = "SELECT * FROM roles WHERE name='ADMIN'";
}
