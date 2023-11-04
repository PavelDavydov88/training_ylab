package org.davydov.service;

import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.davydov.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.davydov.repository.AuthRepository;
import org.davydov.repository.PlayerRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final AuthRepository authRepository = mock(AuthRepository.class);

    AuthService authService = new AuthServiceImpl(playerRepository, authRepository);

    @Test
    public void testThatDoAuthorization() throws SQLException {
        when(playerRepository.findByNamePassword(new PlayerDTO("Pavel", "password"))).thenReturn(new Player(1, "Pavel", "password", 0));
        Optional<String> token = authService.doAuthorization(new PlayerDTO("Pavel", "password"));
        String player = authService.decodeJWT(token.get()).getSubject();
        assertEquals("id=1, name=Pavel, account=0", player);
    }

    @Test
    public void testThatFailAuthorization() throws SQLException {
        when(playerRepository.findByNamePassword(new PlayerDTO(
                "wrongName", "wrongPassword"))).
                thenThrow(new SQLException("this player doesn't exist"));
        assertThrows(SQLException.class, () -> authService.doAuthorization(
                new PlayerDTO("wrongName", "wrongPassword")));

    }
}