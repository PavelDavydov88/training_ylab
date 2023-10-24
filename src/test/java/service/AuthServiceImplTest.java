package service;

import model.Player;
import model.PlayerDTO;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import repository.AuditRepository;
import repository.AuthRepository;
import repository.PlayerRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);
    private final AuditRepository auditRepository = mock(AuditRepository.class);

    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);

    @Test
    public void testThatDoAuthorization() throws SQLException {
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(new Player(1, "Pavel", "password", 0));
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        String player = authService.decodeJWT(token.get()).getSubject();
        assertThat(player).isEqualTo("id=1, name=Pavel, account=0");
    }

    @Test
    public void testThatFailAuthorization() throws SQLException {
        when(playerRepository.findByNamePassword(new PlayerDTO("wrongName", "wrongPassword"))).thenThrow(new  SQLException("this player doesn't exist"));
        Throwable throwable = catchThrowable(() -> {
            authService.doAuthorization(new PlayerDTO("wrongName", "wrongPassword"));;
        });
        assertThat(throwable.getMessage()).isEqualTo("java.sql.SQLException: this player doesn't exist");
    }

}