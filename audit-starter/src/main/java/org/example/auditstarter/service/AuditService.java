package org.example.auditstarter.service;

import java.sql.SQLException;
import java.util.List;

public interface AuditService {
    void sendEvent(long playerId, String auditMessage) throws SQLException;

    List<String> getListAuditAction(long id) throws SQLException, Exception;
}
