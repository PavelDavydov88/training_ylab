package service;

import lombok.RequiredArgsConstructor;
import repository.AuditRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет сервис аудита игрока
 */
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
     * Метод возвращает список событий игрока из репозитория
     *
     * @param playerId ID игрока
     * @return коллекция событий
     * @throws SQLException
     */
    @Override
    public List<String> getEvents(long playerId) throws Exception {
        return auditRepository.findAllById(playerId);
    }
}
