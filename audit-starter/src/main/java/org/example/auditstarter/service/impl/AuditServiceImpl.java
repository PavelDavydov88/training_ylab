package org.example.auditstarter.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auditstarter.repository.AuditRepository;
import org.example.auditstarter.service.AuditService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public void sendEvent(long playerId, String auditMessage) throws SQLException {
        auditRepository.save(playerId, auditMessage);
    }

    @Override
    public List<String> getListAuditAction(long id) throws SQLException, Exception {
        return auditRepository.findAllById(id);
    }
}