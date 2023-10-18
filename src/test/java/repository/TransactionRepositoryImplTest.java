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

public class TransactionRepositoryImplTest {

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    TransactionRepository transactionRepository;

    public static final String INSERT_TRANSACTION = "INSERT INTO wallet.\"transaction\" (\"id\" ,\"name_transaction\") VALUES (nextval( 'wallet.sequence_transaction'), '1')";

    @Before
    public void setUp() throws SQLException, LiquibaseException {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        transactionRepository = new TransactionRepositoryImpl(dbConnectionProvider);
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
        statement.executeUpdate(INSERT_TRANSACTION);
    }

    @Test
    public void testThatSaveTransaction() throws SQLException {
        transactionRepository.save("2");
        String transaction = transactionRepository.find("2");
        assertThat(transaction).isEqualTo("2");
    }

    @Test
    public void thatFindByNameTransaction() throws SQLException {
        String transaction = transactionRepository.find("1");
        assertThat(transaction).isEqualTo("1");
    }

    @Test
    public void thatFindByNameTransactionReturnNull() throws SQLException {
        String transaction = transactionRepository.find("0");
        assertThat(transaction).isNull();
    }
}