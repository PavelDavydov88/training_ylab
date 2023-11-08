package org.example.auditstarter.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auditstarter.repository.AuditRepository;
import org.example.auditstarter.service.AuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
//@ConditionalOnMissingBean(AuditService.class)
public class AuditServiceImpl implements AuditService {

    @Qualifier("auditRepositoryImplStarter")
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