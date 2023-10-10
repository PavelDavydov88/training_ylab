package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryCreditDebitRepositoryImpl implements HistoryCreditDebitRepository {

    private static final Map<Long, List<String>> mapHistoryCreditDebit = new ConcurrentHashMap<>();

    @Override
    public String save(long idPlayer, String historyText) {
        List<String> listHistoryPlayer = mapHistoryCreditDebit.get(idPlayer);
        if (listHistoryPlayer == null) {
            listHistoryPlayer = new ArrayList<>();
        }
        String historyWithDate = historyText + ", time = " + new Date();
        listHistoryPlayer.add(historyWithDate);
        mapHistoryCreditDebit.put(idPlayer, listHistoryPlayer);
        return historyWithDate;
    }

    @Override
    public List<String> findById(long idPlayer) {
        return mapHistoryCreditDebit.get(idPlayer);
    }
}
