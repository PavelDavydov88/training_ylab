package service;

import model.PlayerDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlayerService {

    long getAccount(String token) throws SQLException;

    long debitAccount(String token, long valueDebitAccount, Long transaction) throws Exception;

    long creditAccount(String token, long valueCreditAccount, Long transaction) throws Exception;

    void create(PlayerDTO dto) throws SQLException;

    List<String> getListOperationAccount(String token) throws SQLException;

    List<String> getListAuditAction(String token) throws SQLException;
}
