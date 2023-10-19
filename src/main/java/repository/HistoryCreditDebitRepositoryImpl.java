package repository;

import config.DBConnectionProvider;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * класс для хранения истории дебит/кредит
 */
@RequiredArgsConstructor
public class HistoryCreditDebitRepositoryImpl implements HistoryCreditDebitRepository {
    private final DBConnectionProvider dbConnectionProvider;
    public static final String INSERT_HISTORY = "INSERT INTO wallet.\"history-credit-debit\" (\"id\", \"id_player\", \"operation\") VALUES (nextval( 'wallet.sequence_history'), ?, ?)";
    public static final String SELECT_FIND_HISTORY = "select * from wallet.\"history-credit-debit\" where \"id_player\" = ?";

    /**
     * метод сохраняет историю дебит/кредит игрока
     *
     * @param idPlayer    ID игрока
     * @param historyText история дебит/кредит игрока
     * @throws SQLException
     */
    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        String historyWithDate = historyText + ", time = " + new Date();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_HISTORY);
            preparedStatement.setLong(1, idPlayer);
            preparedStatement.setString(2, historyWithDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }

    /**
     * метод возращает истории дебит/кредит игрока
     *
     * @param id ID игрока
     * @return список истории дебит/кредит игрока
     * @throws SQLException
     */
    @Override
    public List<String> findById(long id) throws SQLException {
        List<String> listHistory = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_HISTORY);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            String operation;
            while (resultSet.next()) {
                operation = resultSet.getString("operation");
                listHistory.add(operation);
            }
            return listHistory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    }
}
