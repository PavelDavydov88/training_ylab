package repository;

import java.sql.SQLException;
import java.util.List;

public interface HistoryCreditDebitRepository {
    void save(long idPlayer, String historyText) throws SQLException;

    List<String> findById(long idPlayer) throws SQLException;
}
