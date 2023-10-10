package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import model.Player;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 * класс предоставляет сервис по работе с данными игрока
 */
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TransactionService transactionService;
    private final AuthRepository authRepository;
    private final HistoryCreditDebitRepository historyCreditDebitRepository;
    private final AuditRepository auditRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            TransactionService transactionService,
            AuthRepository authRepository,
            HistoryCreditDebitRepository historyCreditDebitRepository,
            AuditRepository auditRepository) {
        this.playerRepository = playerRepository;
        this.transactionService = transactionService;
        this.authRepository = authRepository;
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
        if (authRepository.find(token) == null) {
            System.out.println("token не действительный");
            return 0;
        }
        int id = Integer.parseInt(decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        auditRepository.save(player.getId(), "запрос account");
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
        if (authRepository.find(token) == null) {
            System.out.println("token не действительный");
            return 0;
        }
        int id = Integer.parseInt(decodeJWT(token).getId());
        Player player = playerRepository.findById(id);

        if (transactionService.checkExist(transaction)) {
            auditRepository.save(player.getId(), "debit не выполнено успешно, такая транзакция существует");
            throw new Exception(transaction + " такая транзакция существует");
        }
        transactionService.save(transaction);
        if (player.getAccount() < valueDebitAccount) {
            auditRepository.save(player.getId(), "debit не выполнено успешно, у игрока не достаточно средств");
            throw new Exception("у игрока не достаточно средств");
        }
        player.setAccount(player.getAccount() - valueDebitAccount);
        historyCreditDebitRepository.save(player.getId(), "debit account - " + valueDebitAccount);
        auditRepository.save(player.getId(), "debit выполнено успешно");
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

        if (authRepository.find(token) == null) {
            System.out.println("token не действительный");
            return 0;
        }
        int id = Integer.parseInt(decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        if (transactionService.checkExist(transaction)) {
            auditRepository.save(player.getId(), "credit не выполнено успешно, такая транзакция существует");
            throw new Exception(transaction + " такая транзакция существует");
        }
        transactionService.save(transaction);
        player.setAccount(player.getAccount() + valueCreditAccount);
        historyCreditDebitRepository.save(player.getId(), "credit account + " + valueCreditAccount);
        auditRepository.save(player.getId(), "credit выполнено успешно");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * метод создания игрока
     * @param inputPlayer входные данные для создания игрока (имя, пароль)
     * @return новый игрок
     */
    @Override
    public Player create(Player inputPlayer) {
        Player player = playerRepository.save(inputPlayer);
        auditRepository.save(player.getId(), "регистрация прошла успешно");
        return player;
    }

    /**
     * метод для получения истории пополнения/снятия средств игроком
     * @param token  токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListOperationAccount(String token) {
        if (authRepository.find(token) == null) {
            System.out.println("token не действительный");
            return null;
        }
        int id = Integer.parseInt(decodeJWT(token).getId());
        auditRepository.save(id, "запрос истории операций credit/debit");
        return historyCreditDebitRepository.findById(id);
    }

    /**
     * метод для получениия из репозитория игрока по имени и паролю
     * @param name имя игрока
     * @param password пароль игрока
     * @return возращает игрока
     */
    @Override
    public Player findByNamePassword(String name, String password) {
        return playerRepository.findByNamePassword(name, password);
    }

    /**
     * метод для получения аудита действий игрока
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListAuditAction(String token) {
        if (authRepository.find(token) == null) {
            System.out.println("token не действительный");
            return null;
        }
        int id = Integer.parseInt(decodeJWT(token).getId());
        return auditRepository.findById(id);
    }

    /**
     * метод для получениия из репозитория игрока по токену
     * @param token токен игрока
     * @return возращает игрока
     */
    @Override
    public Player findByToken(String token) {
        int id = Integer.parseInt(decodeJWT(token).getId());
        return playerRepository.findById(id);
    }

    /**
     * метод декодинга токена
     * @param jwt токен
     * @return расшифрованный токен
     */
    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("SECRET_KEY"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
