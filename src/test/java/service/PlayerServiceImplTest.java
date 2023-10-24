package service;

import model.AccountOperationDTO;
import model.Player;
import model.PlayerDTO;
import org.junit.jupiter.api.Test;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final HistoryCreditDebitRepository historyCreditDebitRepository = mock(HistoryCreditDebitRepository.class);
    private final AuditService auditService = mock(AuditService.class);
    private final TransactionService transactionService = mock(TransactionService.class);

    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);
    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitRepository, auditService);

    @Test
    public void testThatDoneCreate() throws SQLException {
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        playerService.create(new PlayerDTO("Pavel", "password"));
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    public void testThatGetAccount() throws SQLException {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        long account = playerService.getAccount(token.get());
        assertThat(account).isEqualTo(0);
    }

    @Test
    public void testThatFailDoDebit() throws SQLException {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        Throwable throwable = catchThrowable(() -> {
            playerService.debitAccount(token.get(),new AccountOperationDTO(1L, 100L));
        });
        assertThat(throwable.getMessage()).isEqualTo("the player doesn't have enough money");
    }

    @Test
    public void testThatDoDebit() throws SQLException {
        when(authRepository.find(anyString())).thenReturn(Optional.of("1"));
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(createDefaultPlayer());
        when(playerRepository.update(any(Player.class))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        try {
            long account = playerService.debitAccount(token.get(), createDefaultOperationDTO());
            assertThat(account).isEqualTo(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Player createDefaultPlayer() {
        return new Player(1, "Pavel", "password", 0);
    }
    AccountOperationDTO createDefaultOperationDTO() {
        return new AccountOperationDTO( 1L,1L);
    }
}