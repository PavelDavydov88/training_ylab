package service;

import model.Player;
import org.junit.Test;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.PlayerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);

    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);

    @Test
    public void testThatDoAuthorization() {
        when(playerRepository.findByNamePassword("Pavel", "password")).thenReturn(new Player("Pavel", "password", 1));
        String token = authService.doAuthorization("Pavel", "password");
        String player = authService.decodeJWT(token).getSubject();
        assertThat(player).isEqualTo("id=1, name=Pavel, account=0");
    }

    @Test
    public void testThatFailAuthorization() {
        when(playerRepository.findByNamePassword("Pavel", "password")).thenReturn(null);
        String wrongToken = authService.doAuthorization("wrongName", "wrongPassword");
        assertThat(wrongToken).isEqualTo("this player doesn't exist");
    }




}