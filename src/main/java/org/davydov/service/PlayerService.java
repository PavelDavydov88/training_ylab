package org.davydov.service;

import org.davydov.model.AccountOperationDTO;
import org.davydov.model.PlayerDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlayerService {

    long getAccount(long idPlayer, String token) throws SQLException;

    long debitAccount(long idPlayer, AccountOperationDTO dto, String token) throws Exception;

    long creditAccount(long idPlayer, AccountOperationDTO dto, String token) throws Exception;

    long create(PlayerDTO dto) throws SQLException;

    List<String> getListAuditAction(long idPlayer, String token) throws SQLException, Exception;

}
