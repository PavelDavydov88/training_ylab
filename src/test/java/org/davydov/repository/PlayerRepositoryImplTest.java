package org.davydov.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class PlayerRepositoryImplTest {

    @Container
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    PlayerRepository playerRepository;
    public static final String INSERT_PLAYER = """
            INSERT INTO wallet."player" ("id", user_name, password, account)
            VALUES (nextval( 'wallet.sequence_player'), 'Pavel', '123', 0)""";

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
        dbConnectionProvider.setUsername(postgresContainer.getUsername());
        dbConnectionProvider.setPassword(postgresContainer.getPassword());
        dbConnectionProvider.setUrl(postgresContainer.getJdbcUrl());
        playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
        Connection connection = DriverManager
                .getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        String sql = "CREATE SCHEMA IF NOT EXISTS liquibase";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        database.setDefaultSchemaName("liquibase");
        Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();

        connection = DriverManager
                .getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        statement = connection.createStatement();
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