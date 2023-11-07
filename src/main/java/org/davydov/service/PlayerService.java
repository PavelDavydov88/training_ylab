package org.davydov.service;

import org.davydov.model.AccountOperationDTO;
import org.davydov.model.PlayerDTO;

import java.sql.SQLException;

public interface PlayerService {

    long getAccount(long idPlayer, String token) throws SQLException;

    long debitAccount(AccountOperationDTO dto, String token) throws Exception;

    long creditAccount(AccountOperationDTO dto, String token) throws Exception;

    long create(PlayerDTO dto) throws SQLException;

}
