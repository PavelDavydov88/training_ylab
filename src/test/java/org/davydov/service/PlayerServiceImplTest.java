package org.davydov.service;

import lombok.SneakyThrows;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.AuthDTO;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.davydov.repository.AuthRepository;
import org.davydov.repository.PlayerRepository;
import org.davydov.service.impl.AuthServiceImpl;
import org.davydov.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);
    private final HistoryCreditDebitService historyCreditDebitService = mock(HistoryCreditDebitService.class);
    private final AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private final PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitService);

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
        Optional<String> token = authService.doAuthorization(new AuthDTO("Pavel", "password", 1L));
        long account = playerService.getAccount(1L, token.get());
        assertEquals(0, account);
    }

    @SneakyThrows
    @Test
    public void testThatFailDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new AuthDTO("Pavel", "password", 1));
        assertThrows(Exception.class, () -> playerService.debitAccount(new AccountOperationDTO(1L,100L, 16L), token.get()));
    }

    @SneakyThrows
    @Test
    public void testThatDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.update(any(Player.class))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new AuthDTO("Pavel", "password", 1L));
        Assertions.assertThrows(Exception.class, () -> playerService.debitAccount(createDefaultOperationDTO(), token.get()));
    }

    Player createDefaultPlayer() {
        return new Player(1, "Pavel", "password", 0);
    }

    AccountOperationDTO createDefaultOperationDTO() {
        return new AccountOperationDTO(1L, 1L, 1L);
    }
}