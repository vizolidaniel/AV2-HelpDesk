package br.com.daniel.dao;

import br.com.daniel.domain.ServiceRequestComment;
import br.com.daniel.exception.UpdateException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.daniel.dao.statements.ServiceRequestCommnenDAOStatements.INSERT_NEW;
import static br.com.daniel.dao.statements.ServiceRequestCommnenDAOStatements.SELECT_ALL_BY_REQUEST_ID;

@Repository
public class ServiceRequestCommentDAO {
    private final JdbcTemplate jdbcTemplate;

    public ServiceRequestCommentDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final ServiceRequestComment serviceRequestComment) {
        final Map<String, Object> insertingData = new HashMap<>();
        insertingData.put("id", serviceRequestComment.getId());
        insertingData.put("comment", serviceRequestComment.getComment());
        insertingData.put("service_requests_id", serviceRequestComment.getServiceRequestsId());
        insertingData.put("updated_by", serviceRequestComment.getUpdatedBy());
        insertingData.put("updated_at", serviceRequestComment.getUpdatedAt());
        insertingData.put("created_by", serviceRequestComment.getCreatedBy());
        insertingData.put("created_at", serviceRequestComment.getCreatedAt());

        final List<String> keys = new ArrayList<>(insertingData.keySet());

        final String insertPattern = String.join(",", keys);
        final String valuesPattern = keys.stream().map(k -> "?").collect(Collectors.joining(","));

        final String sql = String.format(INSERT_NEW, insertPattern, valuesPattern);

        try {
            final int updatedRows = this.jdbcTemplate.update(sql, ps -> {
                for (int i = 0; i < insertingData.size(); i++) {
                    String key = keys.get(i);
                    Object value = insertingData.get(key);
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
            if (updatedRows < 1) throw new UpdateException("Chamado não foi aberto", "/requests/my");
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage(), "/");
        }
    }

    public List<ServiceRequestComment> findAllByServiceRequestId(final String serviceRequestId) {
        return this.jdbcTemplate.query(
                SELECT_ALL_BY_REQUEST_ID,
                ps -> ps.setString(1, serviceRequestId)
                , (rs, rowNum) -> new ServiceRequestComment(
                        rs.getString("id"),
                        rs.getDate("created_at"),
                        rs.getString("updated_by"),
                        rs.getDate("created_at"),
                        rs.getString("created_by"),
                        rs.getString("comment"),
                        rs.getString("service_requests_id")
                )
        );
    }
}
