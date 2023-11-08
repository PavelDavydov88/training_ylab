package org.example.auditstarter.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@ConditionalOnMissingBean(AuditRepository.class)
public class AuditRepositoryImplStarter implements AuditRepository {

    private final Map<Long, List<String>> map = new HashMap<>();

    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        val temp = map.get(idPlayer);
        temp.add(historyText);
        map.put(idPlayer, temp);
    }

    @Override
    public List<String> findAllById(long idPlayer) throws SQLException, Exception {
        return map.get(idPlayer);
    }
}