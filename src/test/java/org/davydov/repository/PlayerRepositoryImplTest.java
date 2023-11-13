package org.davydov.repository;

import liquibase.exception.LiquibaseException;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerRepositoryImplTest extends RepositoryLiquibaseInit {

    @Autowired
    PlayerRepository playerRepository;
    public static final String INSERT_PLAYER = """
            INSERT INTO wallet."player" ("id", user_name, password, account)
            VALUES (nextval( 'wallet.sequence_player'), 'Pavel', '123', 0)
            """;

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        super.setUp();
        statement.executeUpdate(INSERT_PLAYER);
    }

    @Test
    public void thatSavePlayer() throws SQLException {
        Player getDefaultPlayer = getDefaultPlayer();
        playerRepository.save(getDefaultPlayer);
        Player player = playerRepository.findByNamePassword(getDefaultPlayerDto());
        assertEquals(11, player.getId());
        assertEquals("Ivan", player.getName());
    }

    @Test
    public void thatFindById() throws SQLException {
        Player player = playerRepository.findById(10);
        assertEquals("Pavel", player.getName());
    }

    private Player getDefaultPlayer() {
        return new Player(0, "Ivan", "789", 0);
    }

    private PlayerDTO getDefaultPlayerDto() {
        return new PlayerDTO("Ivan", "789");
    }
}