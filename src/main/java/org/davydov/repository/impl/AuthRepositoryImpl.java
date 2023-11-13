package org.davydov.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.davydov.config.DBConnectionProvider;
import org.davydov.repository.AuthRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO for token
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final DBConnectionProvider dbConnectionProvider;

    private static final String INSERT_TOKEN = """
            INSERT INTO wallet."auth" ("id" ,"token") VALUES (nextval( 'wallet.sequence_auth'), ?)""";
    private static final String SELECT_FIND_TOKEN = """
            select * from wallet."auth" where "token" = ?""";
    private static final String DELETE_TOKEN = """
            delete from wallet."auth" where "token" = ?""";

    /**
     * Метод сохраняет токен
     *
     * @param token токен
     * @throws SQLException
     */
    @Override
    public void save(String token) throws SQLException {
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TOKEN)) {
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает токен
     *
     * @param token токен
     * @return токен опционально
     * @throws SQLException
     */
    @Override
    public Optional<String> find(String token) throws SQLException {
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FIND_TOKEN)) {
            preparedStatement.setString(1, token);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("token"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод удаляет токен
     *
     * @param token токен
     * @throws SQLException
     */
    @Override
    public void delete(String token) throws SQLException {
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TOKEN)) {
            preparedStatement.setString(1, token);
            System.out.println(preparedStatement);
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                log.info("this token don't exist");
            }
        }
    }
}
