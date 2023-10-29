package org.davydov.repository;

import java.sql.SQLException;

public interface TransactionRepository {

    void save(Long idPlayer, Long transaction) throws SQLException;

    Long find(Long transaction) throws SQLException;

}
