package org.davydov.repository;

import org.davydov.config.DBConnectionProvider;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.davydov.repository.AuthRepository;
import org.davydov.repository.AuthRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class AuthRepositoryImplTest {
    @Container
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    AuthRepository authRepository;
    public static final String INSERT_TOKEN = """
            INSERT INTO wallet."auth" ("id" ,"token") VALUES (nextval( 'wallet.sequence_auth'), '1')""";

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
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
        Optional<String> token = authRepository.find("2");
        assertEquals("2", token.get());
    }

    @Test
    public void thatFindByToken() throws SQLException {
        Optional<String> token = authRepository.find("1");
        assertEquals("1", token.get());
    }

    @Test
    public void thatDeleteToken() throws SQLException {
        authRepository.delete("1");
        Optional<String> token = authRepository.find("1");
        assertTrue(token.isEmpty());
    }
}