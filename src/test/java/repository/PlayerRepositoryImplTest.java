package repository;

import config.ConnectionUtils;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import model.Player;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerRepositoryImplTest {

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    PlayerRepository playerRepository = new PlayerRepositoryImpl();


    MockedStatic<ConnectionUtils> connectionUtilsMockedStatic = Mockito.mockStatic(ConnectionUtils.class);



    Connection getTestConnection() {
            String jdbcUrl = postgresContainer.getJdbcUrl();
            String username = postgresContainer.getUsername();
            String password = postgresContainer.getPassword();
            try {
                Connection connection = DriverManager
                        .getConnection(jdbcUrl, username, password);
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                String sql = "CREATE SCHEMA IF NOT EXISTS liquibase";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                database.setDefaultSchemaName("liquibase");
                Liquibase liquibase = new Liquibase("db/test_changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.update();
                return connection;
            } catch (SQLException | LiquibaseException e) {
                throw new RuntimeException(e);
            }
    }


    @Test
    public void thatSavePlayer() throws SQLException, IOException {
        Player defaultPlayer = getDefaultPlayer();

        connectionUtilsMockedStatic.when(ConnectionUtils::getConnection).thenReturn(getTestConnection());
        playerRepository.save(defaultPlayer);
        connectionUtilsMockedStatic.when(ConnectionUtils::getConnection).thenReturn(getTestConnection());
        Player player = playerRepository.findByNamePassword(defaultPlayer.getName(), defaultPlayer.getPassword());
        System.out.println(player);
        assertThat(player.getId()).isEqualTo(11);
        assertThat(player.getName()).isEqualTo("Ivan");
    }

    @Test
    public void thatFindById() throws SQLException {
        connectionUtilsMockedStatic.when(ConnectionUtils::getConnection).thenReturn(getTestConnection());
        Player player = playerRepository.findById(10);
        System.out.println(player);
        assertThat(player.getName()).isEqualTo("Pavel");
    }

    private Player getDefaultPlayer() {
        return new Player(0, "Ivan", "789", 0);
    }
}