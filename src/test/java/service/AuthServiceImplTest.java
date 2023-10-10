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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final HistoryCreditDebitRepository historyCreditDebitRepository = mock(HistoryCreditDebitRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);

    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authRepository, historyCreditDebitRepository, auditRepository);
    AuthService authService = new AuthServiceImpl(playerService, authRepository, auditRepository);

    @Test
    public void doAuthorization() {
        when(playerService.findByNamePassword("Pavel", "password")).thenReturn(new Player("Pavel", "password", 1));
        String token = authService.doAuthorization("Pavel", "password");
        String wrongToken = authService.doAuthorization("wrongName", "wrongPassword");
        String player = decodeJWT(token).getSubject();
        assertThat(player).isEqualTo("id=1, name=Pavel, account=0");
        assertThat(wrongToken).isEqualTo("нет такого игрока");

    }

    public static Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("SECRET_KEY"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}