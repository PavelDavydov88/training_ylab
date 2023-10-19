package service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * класс предоставляет сервис по работе с данными игрока
 */
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TransactionService transactionService;
    private final AuthService authService;
    private final HistoryCreditDebitRepository historyCreditDebitRepository;
    private final AuditService auditService;

    /**
     * Метод возвращает значение счета игрока
     *
     * @param token - токен игрока
     * @return значение счета игрока
     */
    @Override
    public long getAccount(String token) throws SQLException {
        if (authService.find(token) == null) {
            log.info("invalid token");
            return 0;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        Player player = null;
        try {
            player = playerRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        auditService.sendEvent(player.getId(), "getting account");
        return player.getAccount();
    }

    /**
     * метод для выполнения операции debit
     *
     * @param token             токен игрока
     * @param valueDebitAccount значение списания со счета игрока
     * @param transaction       уникальный номер транзакции
     * @return возращает счета игрока
     * @throws Exception в случае значение списания больше значения счета,
     *                   или невалидной транзакции
     */
    @Override
    public long debitAccount(String token, long valueDebitAccount, String transaction) throws Exception {
        if (authService.find(token) == null) {
            log.info("invalid token");
            return 0;
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);

        if (transactionService.checkExist(transaction)) {
            auditService.sendEvent(player.getId(), "debit operation have not been done, this transaction is exist");
            throw new Exception(transaction + " this transaction is exist");
        }
        transactionService.save(id, transaction);
        if (player.getAccount() < valueDebitAccount) {
            auditService.sendEvent(player.getId(), "debit operation have not been done, the player doesn't have enough money");
            throw new Exception("the player doesn't have enough money");
        }
        player.setAccount(player.getAccount() - valueDebitAccount);
        historyCreditDebitRepository.save(player.getId(), "debit account - " + valueDebitAccount);
        auditService.sendEvent(player.getId(), "debit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * метод для выполнения операции credit
     *
     * @param token              токен игрока
     * @param valueCreditAccount значение списания со счета игрока
     * @param transaction        уникальный номер транзакции
     * @return возращает счета игрока
     * @throws Exception в случае невалидной транзакции
     */
    @Override
    public long creditAccount(String token, long valueCreditAccount, String transaction) throws Exception {

        if (authService.find(token) == null) {
            log.info("invalid token");
            return 0;
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        if (transactionService.checkExist(transaction)) {
            auditService.sendEvent(player.getId(), "credit operation have not been done, this transaction is exist");
            throw new Exception(transaction + " this transaction is exist");
        }
        transactionService.save(id, transaction);
        player.setAccount(player.getAccount() + valueCreditAccount);
        historyCreditDebitRepository.save(player.getId(), "credit account + " + valueCreditAccount);
        auditService.sendEvent(player.getId(), "credit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }


    /**
     * метод создания игрока
     *
     * @param name     имя
     * @param password пароль
     * @throws SQLException
     */
    @Override
    public void create(String name, String password) throws SQLException {
        Player inputPlayer = new Player(0, name, password, 0);
        try {
            playerRepository.save(inputPlayer);
            Player player = playerRepository.findByNamePassword(name, password);
            auditService.sendEvent(player.getId(), "registration completed successful");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для получения истории пополнения/снятия средств игроком
     *
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListOperationAccount(String token) throws SQLException {
        if (authService.find(token) == null) {
            log.info("invalid token");
            return null;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        auditService.sendEvent(id, "operation request history of credit/debit operations");
        try {
            return historyCreditDebitRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для получения аудита действий игрока
     *
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListAuditAction(String token) throws SQLException {
        if (authService.find(token) == null) {
            log.info("invalid token");
            return null;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        return auditService.getEvents(id);
    }
}
