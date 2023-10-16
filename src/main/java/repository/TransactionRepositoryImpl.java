package repository;

import config.ConnectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public void save(String transaction) throws SQLException {
        String insertDataSQL = "INSERT INTO wallet.\"transaction-liquibase\" (\"nameTransaction\") VALUES (?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setString(1, transaction);
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
    public String find(String transaction) throws SQLException {
        String insertDataSQL = "select * from wallet.\"transaction-liquibase\" where \"nameTransaction\" = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setString(1, transaction);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nameTransaction");
            } else {
                return null;
            }
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
