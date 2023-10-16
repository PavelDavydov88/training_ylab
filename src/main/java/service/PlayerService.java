package service;

import java.sql.SQLException;
import java.util.List;

public interface PlayerService {

    long getAccount(String token) throws SQLException;

    long debitAccount(String token, long valueDebitAccount, String transaction) throws Exception;

    long creditAccount(String token, long valueCreditAccount, String transaction) throws Exception;

    void create(String name, String password) throws SQLException;

    List<String> getListOperationAccount(String token) throws SQLException;

    List<String> getListAuditAction(String token) throws SQLException;
}
