package org.davydov.service;

import lombok.SneakyThrows;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.davydov.repository.AuthRepository;
import org.davydov.repository.PlayerRepository;
import org.davydov.service.impl.AuthServiceImpl;
import org.davydov.service.impl.PlayerServiceImpl;
import org.example.auditstarter.repository.AuditRepository;
import org.example.auditstarter.service.AuditService;
import org.example.auditstarter.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);
    private final HistoryCreditDebitService historyCreditDebitService = mock(HistoryCreditDebitService.class);
    private final AuthService authService = new AuthServiceImpl(playerRepository, authRepository);

    private final AuditService auditService = new AuditServiceImpl(auditRepository);

    private final PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitService, auditService);

    @SneakyThrows
    @Test
    public void testThatDoneCreate() {
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        playerService.create(new PlayerDTO("Pavel", "password"));
        verify(playerRepository).save(any(Player.class));
    }

    @SneakyThrows
    @Test
    public void testThatGetAccount() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(1L, new PlayerDTO("Pavel", "password"));
        long account = playerService.getAccount(1L, token.get());
        assertEquals(0, account);
    }

    @SneakyThrows
    @Test
    public void testThatFailDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(1L, new PlayerDTO("Pavel", "password"));
        assertThrows(Exception.class, () -> playerService.debitAccount(1L, new AccountOperationDTO(100L, 16L), token.get()));
    }

    @SneakyThrows
    @Test
    public void testThatDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.update(any(Player.class))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(1L, new PlayerDTO("Pavel", "password"));
        Assertions.assertThrows(Exception.class, () -> playerService.debitAccount(1L, createDefaultOperationDTO(), token.get()));
    }

    Player createDefaultPlayer() {
        return new Player(1, "Pavel", "password", 0);
    }

    AccountOperationDTO createDefaultOperationDTO() {
        return new AccountOperationDTO(1L, 1L);
    }
}