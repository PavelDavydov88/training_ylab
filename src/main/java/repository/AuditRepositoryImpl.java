package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuditRepositoryImpl implements AuditRepository {

    private static final Map<Long, List<String>> mapAudit = new ConcurrentHashMap<>();

    @Override
    public String save(long idPlayer, String historyText) {
        List<String> listAuditPlayer = mapAudit.get(idPlayer);
        if (listAuditPlayer == null) {
            listAuditPlayer = new ArrayList<>();
        }
        String historyWithDate = historyText + ", time = " + new Date();
        listAuditPlayer.add(historyWithDate);
        mapAudit.put(idPlayer, listAuditPlayer);
        return historyWithDate;
    }

    @Override
    public List<String> findById(long idPlayer) {
        return mapAudit.get(idPlayer);
    }
}
