package service;

import lombok.SneakyThrows;
import model.AccountOperationDTO;
import model.Player;
import model.PlayerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import java.sql.SQLException;
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
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        long account = playerService.getAccount(token.get());
        assertEquals(0, account);
    }

    @SneakyThrows
    @Test
    public void testThatFailDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        assertThrows(Exception.class, () -> playerService.debitAccount(token.get(), new AccountOperationDTO(1L, 100L)));
    }

    @SneakyThrows
    @Test
    public void testThatDoDebit() {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.update(any(Player.class))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        Assertions.assertThrows(Exception.class, () -> playerService.debitAccount(token.get(), createDefaultOperationDTO()));
    }

    Player createDefaultPlayer() {
        return new Player(1, "Pavel", "password", 0);
    }

    AccountOperationDTO createDefaultOperationDTO() {
        return new AccountOperationDTO(1L, 1L);
    }
}