package org.davydov.service;

import java.sql.SQLException;
import java.util.List;

public interface HistoryCreditDebitService {
    void sendHistory(long playerId, String historyText) throws SQLException;

    List<String> getListOperationAccount(String token) throws Exception;
}
