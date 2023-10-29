package org.davydov.service;

import org.davydov.model.AccountOperationDTO;
import org.davydov.model.PlayerDTO;

import java.sql.SQLException;

public interface PlayerService {

    long getAccount(String token) throws SQLException;

    long debitAccount(String token, AccountOperationDTO dto) throws Exception;

    long creditAccount(String token, AccountOperationDTO dto) throws Exception;

    void create(PlayerDTO dto) throws SQLException;

}
