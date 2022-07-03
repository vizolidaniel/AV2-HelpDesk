package br.com.daniel.dao.statements;

public interface ServiceRequestCommnenDAOStatements {
    String INSERT_NEW = "INSERT INTO service_requests_comments (%s) VALUES (%s)";
    String SELECT_ALL_BY_REQUEST_ID = "SELECT * FROM service_requests_comments WHERE service_requests_id=?";
}
