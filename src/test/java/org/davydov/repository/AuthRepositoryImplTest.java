package org.davydov.repository;

import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthRepositoryImplTest extends RepositoryLiquibaseInit {

    @Autowired
    AuthRepository authRepository;
    private static final String INSERT_TOKEN = """
            INSERT INTO wallet."auth" ("id" ,"token") VALUES (nextval( 'wallet.sequence_auth'), '1')
            """;

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        super.setUp();
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