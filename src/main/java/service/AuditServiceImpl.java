package service;

import lombok.RequiredArgsConstructor;
import repository.AuditRepository;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    /**
     * метод записывает события в репозиторий
     * @param playerId  ID игрока
     * @param auditMessage текст события
     * @throws SQLException
     */
    @Override
    public void sendEvent(long playerId, String auditMessage) throws SQLException {
        auditRepository.save(playerId, auditMessage);
    }

    /**
     * метод возращает список событий игрока
     * @param playerId ID игрока
     * @return коллекция событий
     * @throws SQLException
     */
    @Override
    public List<String> getEvents(long playerId) throws SQLException {
        return auditRepository.findAllById(playerId);
    }
}
