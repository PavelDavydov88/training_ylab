package repository;

import java.sql.SQLException;

public interface TransactionRepository {

    void save(String transaction) throws SQLException;

    String find(String transaction) throws SQLException;

}
