package org.davydov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.davydov.repository.AuditRepository;
import org.davydov.service.AuditService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет сервис аудита игрока
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    /**
     * Метод отправляет события для записи в репозиторий
     *
     * @param playerId     ID игрока
     * @param auditMessage текст события
     * @throws SQLException
     */
    @Override
    public void sendEvent(long playerId, String auditMessage) throws SQLException {
        auditRepository.save(playerId, auditMessage);
    }

    /**
     * Метод для получения аудита действий игрока
     *
     * @param idPlayer токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListAuditAction(long idPlayer) throws Exception {
        return auditRepository.findAllById(idPlayer);
    }
}
