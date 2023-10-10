package repository;

import java.util.List;

public interface HistoryCreditDebitRepository {
    String save(long idPlayer, String historyText);

    List<String> findById(long idPlayer);
}
