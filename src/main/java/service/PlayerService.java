package service;

import model.Player;

import java.util.List;

public interface PlayerService {

    long getAccount(String token);

    long debitAccount(String token, long valueDebitAccount, String transaction) throws Exception;

    long creditAccount(String token, long valueCreditAccount, String transaction) throws Exception;

    Player create(Player player);

    List<String> getListOperationAccount(String token);

    Player findByNamePassword(String name, String password);

    Player findByToken(String token);

    List<String> getListAuditAction(String token);
}
