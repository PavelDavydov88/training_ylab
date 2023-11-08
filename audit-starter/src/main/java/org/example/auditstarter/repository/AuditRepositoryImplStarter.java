package org.example.auditstarter.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для хранения аудита игрока
 */
@Repository
@RequiredArgsConstructor
public class AuditRepositoryImplStarter implements AuditRepository {

    private final Map<Long, List<String>> map = new HashMap<>();

    /**
     * Метод сохранения аудита
     *
     * @param idPlayer    ID игрока
     * @param historyText текст события объявленного в аннотации
     * @throws SQLException
     */
    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        val temp = map.get(idPlayer);
        temp.add(historyText);
        map.put(idPlayer, temp);
    }

    /**
     * Метод возращает список аудита игрока
     *
     * @param idPlayer ID игрока
     * @return список аудита
     * @throws SQLException
     * @throws Exception
     */
    @Override
    public List<String> findAllById(long idPlayer) throws SQLException, Exception {
        return map.get(idPlayer);
    }
}