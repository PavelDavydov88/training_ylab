package org.davydov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.davydov.model.mapper.PlayerMapper;
import org.davydov.repository.PlayerRepository;
import org.davydov.service.*;
import org.example.auditstarter.aop.annotations.Audit;
import org.example.auditstarter.service.AuditService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет сервис по работе с данными игрока
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TransactionService transactionService;
    private final AuthService authService;
    private final HistoryCreditDebitService historyCreditDebitService;
    private final AuditService auditService;

    /**
     * Метод возвращает значение счета игрока
     *
     * @param idPlayer
     * @param token    - токен игрока
     * @return значение счета игрока
     */
    @Audit(success = "getting account")
    @Override
    public long getAccount(long idPlayer, String token) throws SQLException {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new RuntimeException("invalid token");
        }

        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        if (id != idPlayer) {
            log.info("invalid token");
            throw new RuntimeException("invalid token");
        }
        Player player = null;
        try {
            player = playerRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return player.getAccount();
    }

    /**
     * Метод для выполнения операции debit
     *
     * @param idPlayer
     * @param dto      DTO операции игрока
     * @param token    токен игрока
     * @return возращает счет игрока
     * @throws Exception в случае значение списания больше значения счета,
     *                   или невалидной транзакции
     */
    @Audit(success = "debit operation completed")
    @Override
    public long debitAccount(long idPlayer, AccountOperationDTO dto, String token) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);

        if (transactionService.checkExist(dto.getTransaction())) {
            String auditMessage = "debit operation have not been done, this transaction is exist";
            throw new Exception(dto.getTransaction() + auditMessage);
        }
        transactionService.save(id, dto.getTransaction());
        if (player.getAccount() < dto.getValueOperation()) {
            String auditMessage = "debit operation have not been done, the player doesn't have enough money";
            throw new Exception(auditMessage);
        }
        player.setAccount(player.getAccount() - dto.getValueOperation());
        historyCreditDebitService.sendHistory(player.getId(), "debit account - " + dto.getValueOperation());
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * Метод для выполнения операции credit
     *
     * @param idPlayer
     * @param dto      DTO операции игрока
     * @param token    токен игрока
     * @return возращает счета игрока
     * @throws Exception в случае невалидной транзакции
     */
    @Audit(success = "credit operation completed")
    @Override
    public long creditAccount(long idPlayer, AccountOperationDTO dto, String token) throws Exception {

        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        long id = Long.parseLong(authService.decodeJWT(token).getId());
        Player player = playerRepository.findById(id);
        if (transactionService.checkExist(dto.getTransaction())) {
            String auditMessage = "debit operation have not been done, this transaction is exist";
            throw new Exception(dto.getTransaction() + auditMessage);
        }
        transactionService.save(id, dto.getTransaction());
        player.setAccount(player.getAccount() + dto.getValueOperation());
        historyCreditDebitService.sendHistory(player.getId(), "credit account + " + dto.getValueOperation());
        player = playerRepository.update(player);
        return player.getAccount();
    }

    /**
     * Метод создания игрока
     *
     * @param dto DTO данные для создания игрока (name, password)
     * @throws SQLException
     */

    @Audit(success = "registration completed successful")
    @Override
    public long create(PlayerDTO dto) throws SQLException {
        try {
            Player playerInput = PlayerMapper.INSTANCE.toModel(dto);
            playerRepository.save(playerInput);
            return (playerRepository.findByNamePassword(dto).getId());
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Audit(success = "operation request audit of player")
    @Override
    public List<String> getListAuditAction(long idPlayer, String token) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        if (id != idPlayer) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        return auditService.getListAuditAction(id);
    }
}
