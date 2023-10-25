package service;

import aop.annotations.Audit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.AccountOperationDTO;
import model.Player;
import model.PlayerDTO;
import model.mapper.PlayerMapper;
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
    @Audit(success = "getting account")
    @Override
    public long getAccount(String token) throws SQLException {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new RuntimeException("invalid token");
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        Player player = null;
        try {
            player = playerRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        auditService.sendEvent(player.getId(), "getting account");
        return player.getAccount();
    }

    /**
     * метод для выполнения операции debit
     *
     * @param token токен игрока
     * @param dto
     * @return возращает счета игрока
     * @throws Exception в случае значение списания больше значения счета,
     *                   или невалидной транзакции
     */
    @Audit(success = "debit operation completed")
    @Override
    public long debitAccount(String token, AccountOperationDTO dto) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);

        if (transactionService.checkExist(dto.getTransaction())) {
            String auditMessage = "debit operation have not been done, this transaction is exist";
//            auditService.sendEvent(player.getId(), "debit operation have not been done, this transaction is exist");
            throw new Exception(dto.getTransaction() + auditMessage);
        }
        transactionService.save(id, dto.getTransaction());
        if (player.getAccount() < dto.getValueOperation()) {
            String auditMessage = "debit operation have not been done, the player doesn't have enough money";
//            auditService.sendEvent(player.getId(), "debit operation have not been done, the player doesn't have enough money");
            throw new Exception(auditMessage);
        }
        player.setAccount(player.getAccount() - dto.getValueOperation());
        historyCreditDebitRepository.save(player.getId(), "debit account - " + dto.getValueOperation());
//        auditService.sendEvent(player.getId(), "debit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * метод для выполнения операции credit
     *
     * @param token токен игрока
     * @param dto
     * @return возращает счета игрока
     * @throws Exception в случае невалидной транзакции
     */
    @Audit(success = "credit operation completed")
    @Override
    public long creditAccount(String token, AccountOperationDTO dto) throws Exception {

        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        if (transactionService.checkExist(dto.getTransaction())) {
            String auditMessage = "debit operation have not been done, this transaction is exist";
//            auditService.sendEvent(player.getId(), "debit operation have not been done, this transaction is exist");
            throw new Exception(dto.getTransaction() + auditMessage);
        }
        transactionService.save(id, dto.getTransaction());
        player.setAccount(player.getAccount() + dto.getValueOperation());
        historyCreditDebitRepository.save(player.getId(), "credit account + " + dto.getValueOperation());
//        auditService.sendEvent(player.getId(), "credit operation completed");
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * Метод создания игрока
     *
     * @param dto данные для создания игрока (name, password)
     * @throws SQLException
     */

    @Audit(success = "registration completed successful")
    @Override
    public void create(PlayerDTO dto) throws SQLException {
        try {
            Player playerInput = PlayerMapper.INSTANCE.toModel(dto);
            playerRepository.save(playerInput);
            Player player = playerRepository.findByNamePassword(dto);
//            auditService.sendEvent(player.getId(), "registration completed successful");
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * метод для получения истории пополнения/снятия средств игроком
     *
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Audit(success = "operation request history of credit/debit operations")
    @Override
    public List<String> getListOperationAccount(String token) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
//        auditService.sendEvent(id, "operation request history of credit/debit operations");
        try {
            return historyCreditDebitRepository.findById(id);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * метод для получения аудита действий игрока
     *
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Override
    public List<String> getListAuditAction(String token) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        return auditService.getEvents(id);
    }
}
