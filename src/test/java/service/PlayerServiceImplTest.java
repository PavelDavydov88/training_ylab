package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import model.Player;
import org.junit.Test;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.HistoryCreditDebitRepository;
import repository.PlayerRepository;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final HistoryCreditDebitRepository historyCreditDebitRepository = mock(HistoryCreditDebitRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);

    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authRepository, historyCreditDebitRepository, auditRepository);
    AuthService authService = new AuthServiceImpl(playerService, authRepository, auditRepository);

    Player createDefaultPlayer() {
        return new Player("Pavel", "password", 0);
    }

    @Test
    public void create() {
        Player playerDefault = createDefaultPlayer();
        when(playerRepository.save(playerDefault)).thenReturn(new Player("Pavel", "password", 1));
        Player player = playerService.create(playerDefault);
        assertThat(player.getId()).isEqualTo(1);
        verify(playerRepository).save(playerDefault);
    }

    @Test
    public void getAccount() {
        Player playerDefault = createDefaultPlayer();
        when(playerService.findByNamePassword("Pavel", "password")).thenReturn(playerDefault);
        String token = authService.doAuthorization("Pavel", "password");
        when(authRepository.find(token)).thenReturn("1");
        when(playerRepository.findById(0)).thenReturn(playerDefault);
        long account = playerService.getAccount(token);
        assertThat(account).isEqualTo(0);
    }


    public static Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("SECRET_KEY"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}