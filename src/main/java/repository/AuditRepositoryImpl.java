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

@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final DBConnectionProvider dbConnectionProvider;
    public static final String INSERT_AUDIT = "INSERT INTO wallet.\"audit\" (\"id\", \"id_player\", \"operation\") VALUES (nextval( 'wallet.sequence_audit'), ?, ?)";
    public static final String SELECT_FIND_AUDIT = "select * from wallet.\"audit\" where \"id_player\" = ?";

    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        String historyWithDate = historyText + ", time = " + new Date();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_AUDIT);
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

    @Override
    public List<String> findAllById(long id) throws SQLException {
        List<String> listHistory = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_AUDIT);
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
