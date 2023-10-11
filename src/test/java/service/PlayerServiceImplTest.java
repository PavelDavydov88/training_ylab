package service;

import model.Player;
import org.junit.Test;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final HistoryCreditDebitRepository historyCreditDebitRepository = mock(HistoryCreditDebitRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);

    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);
    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitRepository, auditRepository);

    @Test
    public void testThatDoneCreate() {
        when(playerRepository.save(any(Player.class))).thenReturn(createDefaultPlayer());
        Player player = playerService.create("Pavel", "password");
        assertThat(player.getId()).isEqualTo(1);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    public void testThatGetAccount() {
        when(authRepository.find(anyString())).thenReturn("1");
        when(playerRepository.findByNamePassword("Pavel", "password")).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        String token = authService.doAuthorization("Pavel", "password");
        long account = playerService.getAccount(token);
        assertThat(account).isEqualTo(0);
    }

    @Test
    public void testThatFailDoDebit() {
        when(authRepository.find(anyString())).thenReturn("1");
        when(playerRepository.findByNamePassword("Pavel", "password")).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        String token = authService.doAuthorization("Pavel", "password");
        Throwable throwable = catchThrowable(() -> {
            playerService.debitAccount(token, 100, "1");
        });
        assertThat(throwable.getMessage()).isEqualTo("the player doesn't have enough money");
    }

    @Test
    public void testThatDoDebit() {
        when(authRepository.find(anyString())).thenReturn("1");
        when(playerRepository.findByNamePassword("Pavel", "password")).thenReturn(createDefaultPlayer());
        when(playerRepository.update(any(Player.class))).thenReturn(createDefaultPlayer());
        when(playerRepository.findById(1)).thenReturn(createDefaultPlayer());
        String token = authService.doAuthorization("Pavel", "password");
        try {
            long account = playerService.debitAccount(token, 0, "1");
            assertThat(account).isEqualTo(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Player createDefaultPlayer() {
        return new Player("Pavel", "password", 1);
    }

}