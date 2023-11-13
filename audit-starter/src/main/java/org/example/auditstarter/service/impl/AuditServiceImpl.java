package org.example.auditstarter.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auditstarter.repository.AuditRepository;
import org.example.auditstarter.service.AuditService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс обработки аудита
 */
@Component
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    /**
     * Метод отправляет в репозиторий событие аудита
     *
     * @param playerId     ID игрока
     * @param auditMessage текст айдита
     * @throws SQLException
     */
    @Override
    public void sendEvent(long playerId, String auditMessage) throws SQLException {
        auditRepository.save(playerId, auditMessage);
    }

    /**
     * Метод получения списка аудита
     *
     * @param id ID игрока
     * @return список аудита
     * @throws SQLException
     * @throws Exception
     */
    @Override
    public List<String> getListAuditAction(long id) throws SQLException, Exception {
        return auditRepository.findAllById(id);
    }
}