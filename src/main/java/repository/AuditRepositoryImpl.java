package repository;

import config.ConnectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditRepositoryImpl implements AuditRepository {

    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        String historyWithDate = historyText + ", time = " + new Date();
        String insertDataSQL = "INSERT INTO wallet.\"audit-liquibase\" (\"idPlayer\", \"operation\") VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setLong(1, idPlayer);
            preparedStatement.setString(2, historyWithDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }

    @Override
    public List<String> findAllById(long id) throws SQLException {
        List<String> listHistory = new ArrayList<>();
        String insertDataSQL = "select * from wallet.\"audit-liquibase\" where \"idPlayer\" = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    }
}
