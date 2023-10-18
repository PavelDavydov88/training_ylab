package repository;

import config.DBConnectionProvider;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRepositoryImplTest {
    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    AuthRepository authRepository;
    public static final String INSERT_TOKEN = "INSERT INTO wallet.\"auth\" (\"id\" ,\"token\") VALUES (nextval( 'wallet.sequence_auth'), '1')";

    @Before
    public void setUp() throws SQLException, LiquibaseException {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        authRepository = new AuthRepositoryImpl(dbConnectionProvider);
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
        statement.executeUpdate(INSERT_TOKEN);
    }

    @Test
    public void thatSaveToken() throws SQLException {
        authRepository.save("2");
        String token = authRepository.find("2");
        assertThat(token).isEqualTo("2");
    }

    @Test
    public void thatFindByToken() throws SQLException {
        String token = authRepository.find("1");
        assertThat(token).isEqualTo("1");
    }

    @Test
    public void thatDeleteToken() throws SQLException {
        authRepository.delete("1");
        String token = authRepository.find("1");
        assertThat(token).isNull();
    }
}