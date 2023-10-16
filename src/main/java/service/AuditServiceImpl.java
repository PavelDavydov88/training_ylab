package service;

import lombok.RequiredArgsConstructor;
import repository.AuditRepository;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public void sendEvent(long playerId, String auditMessage) throws SQLException {
        auditRepository.save(playerId, auditMessage);
    }

    @Override
    public List<String> getEvents(long playerId) throws SQLException {
        return auditRepository.findAllById(playerId);
    }
}
