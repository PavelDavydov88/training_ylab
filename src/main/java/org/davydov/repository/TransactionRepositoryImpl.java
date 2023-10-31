package org.davydov.repository;

import lombok.RequiredArgsConstructor;
import org.davydov.config.DBConnectionProvider;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для хранения транзакций
 */
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DBConnectionProvider dbConnectionProvider;
    public static final String INSERT_TRANSACTION = """
            INSERT INTO wallet."transaction" ("id", "id_player" ,"transaction") 
            VALUES (nextval( 'wallet.sequence_transaction'), ?, ?)""";
    public static final String SELECT_FIND_TRANSACTION = """
            select * from wallet."transaction" where "transaction" = ?""";

    /**
     * Метод сохраняет транзакцию
     *
     * @param idPlayer    ID игрока
     * @param transaction транзакция
     * @throws SQLException
     */
    @Override
    public void save(Long idPlayer, Long transaction) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_TRANSACTION);
            preparedStatement.setLong(1, idPlayer);
            preparedStatement.setLong(2, transaction);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }

    /**
     * Метод поиска транзакции
     *
     * @param transaction транзакция
     * @return транзакцию. Если не существует, возращает null
     * @throws SQLException
     */
    @Override
    public Long find(Long transaction) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_TRANSACTION);
            preparedStatement.setLong(1, transaction);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Long.valueOf(resultSet.getString("transaction"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    }
}