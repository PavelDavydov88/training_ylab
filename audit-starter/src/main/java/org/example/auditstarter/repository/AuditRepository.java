package org.example.auditstarter.repository;

import java.sql.SQLException;
import java.util.List;

public interface AuditRepository {
    void save(long idPlayer, String historyText) throws SQLException;

    List<String> findAllById(long idPlayer) throws SQLException, Exception;
}
