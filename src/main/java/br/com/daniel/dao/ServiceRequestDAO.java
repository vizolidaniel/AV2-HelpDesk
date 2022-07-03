package br.com.daniel.dao;

import br.com.daniel.dao.statements.ServiceRequestDAOStatements;
import br.com.daniel.domain.ServiceRequest;
import br.com.daniel.domain.ServiceRequestStatus;
import br.com.daniel.exception.UpdateException;
import br.com.daniel.model.Response;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.daniel.dao.statements.ServiceRequestDAOStatements.*;

@Repository
public class ServiceRequestDAO {
    private final JdbcTemplate jdbcTemplate;

    public ServiceRequestDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final ServiceRequest serviceRequest) {
        final Map<String, Object> insertingData = new HashMap<>();
        insertingData.put("id", serviceRequest.getId());
        insertingData.put("description", serviceRequest.getDescription());
        insertingData.put("status", serviceRequest.getStatus().name());
        insertingData.put("updated_by", serviceRequest.getUpdatedBy());
        insertingData.put("updated_at", serviceRequest.getUpdatedAt());
        insertingData.put("created_by", serviceRequest.getCreatedBy());
        insertingData.put("created_at", serviceRequest.getCreatedAt());
        insertingData.put("analyzed_by", serviceRequest.getAnalyzedBy());

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

    public void update(ServiceRequest serviceRequest) {
        final Map<String, Object> updatingData = new HashMap<>();
        updatingData.put("description", serviceRequest.getDescription());
        updatingData.put("status", serviceRequest.getStatus().name());
        updatingData.put("updated_by", serviceRequest.getUpdatedBy());
        updatingData.put("updated_at", serviceRequest.getUpdatedAt());
        updatingData.put("analyzed_by", serviceRequest.getAnalyzedBy());

        final List<String> keys = new ArrayList<>(updatingData.keySet());

        final String updatePattern = keys
                .stream()
                .map(key -> String.format("%s=?", key))
                .collect(Collectors.joining(","));

        final String sql = String.format(ServiceRequestDAOStatements.UPDATE, updatePattern);

        try {
            final int updatedRows = this.jdbcTemplate.update(sql, ps -> {
                for (int i = 0; i < updatingData.size(); i++) {
                    String key = keys.get(i);
                    Object value = updatingData.get(key);
                    if (value instanceof Date) {
                        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.ofInstant(
                                ((Date) value).toInstant(),
                                ZoneId.systemDefault())
                        );
                        ps.setTimestamp(i + 1, timestamp);
                    } else
                        ps.setString(i + 1, (String) value);
                }
                ps.setString(updatingData.size() + 1, serviceRequest.getId());
            });
            if (updatedRows < 1)
                throw new UpdateException(
                        "Nenhuma informação a atualizar",
                        String.format("/requests/%s", serviceRequest.getId())
                );
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage(), String.format("/requests/%s", serviceRequest.getId()));
        }
    }

    public Response<ServiceRequest> findAll(
            final int page,
            final int size,
            final String status
    ) {
        final boolean hasStatusFilter = StringUtils.hasText(status);
        final String SQL = hasStatusFilter ? PAGINATE_ALL_WITH_STATUS : PAGINATE_ALL;
        final List<ServiceRequest> results = this.jdbcTemplate.query(
                SQL,
                ps -> {
                    if (hasStatusFilter) {
                        ps.setString(1, status);
                        ps.setInt(2, size);
                        ps.setInt(3, (page - 1) * size);
                    } else {
                        ps.setInt(1, size);
                        ps.setInt(2, (page - 1) * size);
                    }
                }, (rs, rowNum) -> new ServiceRequest(
                        rs.getString("id"),
                        rs.getTimestamp("created_at"),
                        rs.getString("created_by"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("updated_by"),
                        rs.getString("description"),
                        ServiceRequestStatus.valueOf(rs.getString("status")),
                        rs.getString("analyzed_by")
                )
        );

        final long total = this.jdbcTemplate.query(
                        ServiceRequestDAOStatements.COUNT_ALL,
                        (rs, rowNum) -> rs.getLong(1)
                ).stream()
                .findFirst()
                .orElse(0L);

        return new Response<ServiceRequest>().builder().results(results).page(page).size(size).total(total).build();
    }

    public Response<ServiceRequest> findAllFromUser(
            final int page,
            final int size,
            final String status,
            final String userId
    ) {
        final boolean hasStatusFilter = StringUtils.hasText(status);
        final String SQL = hasStatusFilter ? PAGINATE_ALL_FROM_USER_WITH_STATUS : PAGINATE_ALL_FROM_USER;
        final List<ServiceRequest> results = this.jdbcTemplate.query(
                SQL,
                ps -> {
                    ps.setString(1, userId);
                    if (hasStatusFilter) {
                        ps.setString(2, status);
                        ps.setInt(3, size);
                        ps.setInt(4, (page - 1) * size);
                    } else {
                        ps.setInt(2, size);
                        ps.setInt(3, (page - 1) * size);
                    }
                }, (rs, rowNum) -> new ServiceRequest(
                        rs.getString("id"),
                        rs.getTimestamp("created_at"),
                        rs.getString("created_by"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("updated_by"),
                        rs.getString("description"),
                        ServiceRequestStatus.valueOf(rs.getString("status")),
                        rs.getString("analyzed_by")
                )
        );

        final long total = this.jdbcTemplate.query(
                        ServiceRequestDAOStatements.COUNT_ALL,
                        (rs, rowNum) -> rs.getLong(1)
                ).stream()
                .findFirst()
                .orElse(0L);

        return new Response<ServiceRequest>().builder().results(results).page(page).size(size).total(total).build();
    }

    public Response<ServiceRequest> findAllAnalyzedBy(
            final int page,
            final int size,
            final String status,
            final String analyzerId
    ) {
        String SQL = PAGINATE_ALL_FOR_ANALYZER;
        final boolean hasStatusFilter = StringUtils.hasText(status);
        if (hasStatusFilter) {
            if (ServiceRequestStatus.valueOf(status) == ServiceRequestStatus.OPEN)
                SQL = PAGINATE_ALL_FOR_ANALYZER_WITH_STATUS_OR_OPEN_ONES;
            else
                SQL = PAGINATE_ALL_FOR_ANALYZER_WITH_STATUS;
        }
        final List<ServiceRequest> results = this.jdbcTemplate.query(
                SQL,
                ps -> {
                    ps.setString(1, analyzerId);
                    if (hasStatusFilter) {
                        ps.setString(2, status);
                        ps.setInt(3, size);
                        ps.setInt(4, (page - 1) * size);
                    } else {
                        ps.setInt(2, size);
                        ps.setInt(3, (page - 1) * size);
                    }
                }, (rs, rowNum) -> new ServiceRequest(
                        rs.getString("id"),
                        rs.getTimestamp("created_at"),
                        rs.getString("created_by"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("updated_by"),
                        rs.getString("description"),
                        ServiceRequestStatus.valueOf(rs.getString("status")),
                        rs.getString("analyzed_by")
                )
        );

        final long total = this.jdbcTemplate.query(
                        ServiceRequestDAOStatements.COUNT_ALL,
                        (rs, rowNum) -> rs.getLong(1)
                ).stream()
                .findFirst()
                .orElse(0L);

        return new Response<ServiceRequest>().builder().results(results).page(page).size(size).total(total).build();
    }

    public Optional<ServiceRequest> findById(final String id) {
        final List<ServiceRequest> results = this.jdbcTemplate.query(
                SELECT_BY_ID,
                ps -> {
                    ps.setString(1, id);
                }, (rs, rowNum) -> new ServiceRequest(
                        rs.getString("id"),
                        rs.getTimestamp("created_at"),
                        rs.getString("created_by"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("updated_by"),
                        rs.getString("description"),
                        ServiceRequestStatus.valueOf(rs.getString("status")),
                        rs.getString("analyzed_by")
                )
        );

        if (!results.isEmpty()) return Optional.ofNullable(results.get(0));
        return Optional.empty();
    }
}
