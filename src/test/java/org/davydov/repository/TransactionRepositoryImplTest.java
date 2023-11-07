package org.davydov.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.davydov.config.DBConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TransactionRepositoryImplTest extends RepositoryLiquibaseInit{

    @Autowired
    TransactionRepository transactionRepository;
    public static final String INSERT_TRANSACTION = """
            INSERT INTO wallet."transaction" ("id" , "id_player","transaction")
            VALUES (nextval( 'wallet.sequence_transaction'), 10,'1')
            """;

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        super.setUp();
        statement.executeUpdate(INSERT_TRANSACTION);
    }

    @Test
    public void testThatSaveTransaction() throws SQLException {
        transactionRepository.save(1l, 2L);
        Long transaction = transactionRepository.find(2L);
        assertEquals(2L, transaction);
    }

    @Test
    public void thatFindByNameTransaction() throws SQLException {
        Long transaction = transactionRepository.find(1L);
        assertEquals(1L, transaction);
    }

    @Test
    public void thatFindByNameTransactionReturnNull() throws SQLException {
        Long transaction = transactionRepository.find(0L);
        assertNull(transaction);
    }
}