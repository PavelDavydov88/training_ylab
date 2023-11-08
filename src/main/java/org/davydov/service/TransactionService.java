package org.davydov.service;

import java.sql.SQLException;

public interface TransactionService {

    void save(Long idPlayer, Long transaction) throws SQLException;

    boolean checkExist(Long transaction) throws SQLException;
}
