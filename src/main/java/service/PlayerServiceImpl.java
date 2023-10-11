package service;

import model.Player;
import repository.AuditRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import java.util.List;

/**
 * класс предоставляет сервис по работе с данными игрока
 */
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TransactionService transactionService;
    private final AuthService authService;
    private final HistoryCreditDebitRepository historyCreditDebitRepository;
    private final AuditRepository auditRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            TransactionService transactionService,
            AuthService authService,
            HistoryCreditDebitRepository historyCreditDebitRepository,
            AuditRepository auditRepository) {
        this.playerRepository = playerRepository;
        this.transactionService = transactionService;
        this.authService = authService;
        this.historyCreditDebitRepository = historyCreditDebitRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Метод возвращает значение счета игрока
     * @param token - токен игрока
     * @return значение счета игрока
     */
    @Override
    public long getAccount(String token) {
        if (authService.find(token) == null) {
            System.out.println("invalid token");
            return 0;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        auditRepository.save(player.getId(), "getting account");
        return player.getAccount();
    }

    /**
     * метод для выполнения операции debit
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
            System.out.println("invalid token");
            return 0;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);

        if (transactionService.checkExist(transaction)) {
            auditRepository.save(player.getId(), "debit operation have not been done, this transaction is exist");
            throw new Exception(transaction + " this transaction is exist");
        }
        transactionService.save(transaction);
        if (player.getAccount() < valueDebitAccount) {
            auditRepository.save(player.getId(), "debit operation have not been done, the player doesn't have enough money");
            throw new Exception("the player doesn't have enough money");
        }
        player.setAccount(player.getAccount() - valueDebitAccount);
        historyCreditDebitRepository.save(player.getId(), "debit account - " + valueDebitAccount);
        auditRepository.save(player.getId(), "debit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * метод для выполнения операции credit
     * @param token токен игрока
     * @param valueCreditAccount значение списания со счета игрока
     * @param transaction уникальный номер транзакции
     * @return  возращает счета игрока
     * @throws Exception в случае невалидной транзакции
     */
    @Override
    public long creditAccount(String token, long valueCreditAccount, String transaction) throws Exception {

        if (authService.find(token) == null) {
            System.out.println("invalid token");
            return 0;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        if (transactionService.checkExist(transaction)) {
            auditRepository.save(player.getId(), "credit operation have not been done, this transaction is exist");
            throw new Exception(transaction + " this transaction is exist");
        }
        transactionService.save(transaction);
        player.setAccount(player.getAccount() + valueCreditAccount);
        historyCreditDebitRepository.save(player.getId(), "credit account + " + valueCreditAccount);
        auditRepository.save(player.getId(), "credit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * метод создания игрока
     * @param name имя
     * @param password пароль
     * @return новый игрок
     */
    @Override
    public Player create(String name, String password) {
        Player inputPlayer = new Player(name, password, 0);
        Player player = playerRepository.save(inputPlayer);
        auditRepository.save(player.getId(), "registration completed successful");
        return player;
    }

    /**
     * метод для получения истории пополнения/снятия средств игроком
     * @param token  токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListOperationAccount(String token) {
        if (authService.find(token) == null) {
            System.out.println("invalid token");
            return null;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        auditRepository.save(id, "operation request history of credit/debit operations");
        return historyCreditDebitRepository.findById(id);
    }

    /**
     * метод для получения аудита действий игрока
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListAuditAction(String token) {
        if (authService.find(token) == null) {
            System.out.println("invalid token");
            return null;
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        return auditRepository.findById(id);
    }

}
