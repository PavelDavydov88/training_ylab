package service;

import model.AccountOperationDTO;
import model.PlayerDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlayerService {

    long getAccount(String token) throws SQLException;

    long debitAccount(String token, AccountOperationDTO dto) throws Exception;

    long creditAccount(String token, AccountOperationDTO dto) throws Exception;

    void create(PlayerDTO dto) throws SQLException;

    List<String> getListOperationAccount(String token) throws SQLException;

    List<String> getListAuditAction(String token) throws SQLException;
}
