package service;

import model.Player;

import java.util.List;

public interface PlayerService {

    long getAccount(String token);

    long debitAccount(String token, long valueDebitAccount, String transaction) throws Exception;

    long creditAccount(String token, long valueCreditAccount, String transaction) throws Exception;

    Player create(String name, String password);

    List<String> getListOperationAccount(String token);

    List<String> getListAuditAction(String token);
}
