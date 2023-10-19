package service;

import java.sql.SQLException;

public interface TransactionService {

    void save(Long idPlayer,String transaction) throws SQLException;

    boolean checkExist(String transaction) throws SQLException;
}
