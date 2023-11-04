package org.davydov.repository;

import lombok.RequiredArgsConstructor;
import org.davydov.config.DBConnectionProvider;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO for History debit/credit
 */
@Repository
@RequiredArgsConstructor
public class HistoryCreditDebitRepositoryImpl implements HistoryCreditDebitRepository {

    private final DBConnectionProvider dbConnectionProvider;
    private static final String INSERT_HISTORY = """
            INSERT INTO wallet."history-credit-debit" ("id", "id_player", "operation") VALUES (nextval( 'wallet.sequence_history'), ?, ?)""";
    private static final String SELECT_FIND_HISTORY = """
            select * from wallet."history-credit-debit" where "id_player" = ?""";

    /**
     * Метод сохраняет историю дебит/кредит игрока
     *
     * @param idPlayer    ID игрока
     * @param historyText история дебит/кредит игрока
     * @throws SQLException
     */
    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        String historyWithDate = historyText + ", time = " + new Date();
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_HISTORY)) {
            preparedStatement.setLong(1, idPlayer);
            preparedStatement.setString(2, historyWithDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает истории дебит/кредит игрока
     *
     * @param id ID игрока
     * @return список истории дебит/кредит игрока
     * @throws SQLException
     */
    @Override
    public List<String> findById(long id) throws SQLException {
        List<String> listHistory = new ArrayList<>();
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FIND_HISTORY);) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String operation = resultSet.getString("operation");
                    listHistory.add(operation);
                }
            }
            return listHistory;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
}
