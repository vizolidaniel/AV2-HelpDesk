package br.com.daniel.dao.statements;

public interface ServiceRequestDAOStatements {
    String INSERT_NEW = "INSERT INTO service_requests (%s) VALUES (%s)";
    String UPDATE = "UPDATE service_requests SET %s WHERE id=?";
    String COUNT_ALL = "SELECT COUNT(1) FROM service_requests";
    String PAGINATE_ALL = "SELECT * FROM service_requests LIMIT ? OFFSET ?";
    String PAGINATE_ALL_WITH_STATUS = "SELECT * FROM service_requests WHERE status=? LIMIT ? OFFSET ?";
    String PAGINATE_ALL_FROM_USER = "SELECT * FROM service_requests WHERE created_by=? LIMIT ? OFFSET ?";
    String PAGINATE_ALL_FROM_USER_WITH_STATUS = "SELECT * FROM service_requests WHERE created_by=? AND status=? LIMIT ? OFFSET ?";
    String PAGINATE_ALL_FOR_ANALYZER = "SELECT * FROM service_requests WHERE analyzed_by=? LIMIT ? OFFSET ?";
    String PAGINATE_ALL_FOR_ANALYZER_WITH_STATUS = "SELECT * FROM service_requests WHERE analyzed_by=? AND status=? LIMIT ? OFFSET ?";
    String PAGINATE_ALL_FOR_ANALYZER_WITH_STATUS_OR_OPEN_ONES = "SELECT * FROM service_requests WHERE (analyzed_by=? AND status=?) OR status='OPEN' LIMIT ? OFFSET ?";
    String SELECT_BY_ID = "SELECT * FROM service_requests WHERE id=?";
}
