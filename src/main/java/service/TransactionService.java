package service;

import java.sql.SQLException;

public interface TransactionService {

    void save(String transaction) throws SQLException;

    boolean checkExist(String transaction) throws SQLException;
}
