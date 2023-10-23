package repository;

import config.DBConnectionProvider;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import model.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerRepositoryImplTest {

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    PlayerRepository playerRepository;
    public static final String INSERT_PLAYER = """
            INSERT INTO wallet."player" ("id", user_name, password, account)
            VALUES (nextval( 'wallet.sequence_player'), 'Pavel', '123', 0)""";

    @Before
    public void setUp() throws SQLException, LiquibaseException {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
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
        Player defaultPlayer = getDefaultPlayer();
        playerRepository.save(defaultPlayer);
        Player player = playerRepository.findByNamePassword(defaultPlayer.getName(), defaultPlayer.getPassword());
        assertThat(player.getId()).isEqualTo(11);
        assertThat(player.getName()).isEqualTo("Ivan");
    }

    @Test
    public void thatFindById() throws SQLException {
        Player player = playerRepository.findById(10);
        assertThat(player.getName()).isEqualTo("Pavel");
    }

    private Player getDefaultPlayer() {
        return new Player(0, "Ivan", "789", 0);
    }
}