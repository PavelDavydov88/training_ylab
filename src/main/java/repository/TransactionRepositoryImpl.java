package repository;

import config.ConnectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepositoryImpl implements TransactionRepository {

    public static final String INSERT_TRANSACTION = "INSERT INTO wallet.\"transaction\" (\"id\" ,\"name_transaction\") VALUES (nextval( 'wallet.sequence_transaction'), ?)";
    public static final String SELECT_FIND_TRANSACTION = "select * from wallet.\"transaction\" where \"name_transaction\" = ?";

    @Override
    public void save(String transaction) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_TRANSACTION);
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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_TRANSACTION);
            preparedStatement.setString(1, transaction);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name_transaction");
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
