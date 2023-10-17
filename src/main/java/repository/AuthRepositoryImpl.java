package repository;

import config.ConnectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class AuthRepositoryImpl implements AuthRepository {

    public static final String INSERT_TOKEN = "INSERT INTO wallet.\"auth\" (\"id\" ,\"token\") VALUES (nextval( 'wallet.sequence_auth'), ?)";
    public static final String SELECT_FIND_TOKEN = "select * from wallet.\"auth\" where \"token\" = ?";
    public static final String DELETE_TOKEN = "delete from wallet.\"auth\" where \"token\" = ?";

    @Override
    public void save(String token) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_TOKEN);
            preparedStatement.setString(1, token);
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
    public String find(String token) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_TOKEN);
            preparedStatement.setString(1, token);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("token");
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

    @Override
    public void delete(String token) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_TOKEN);
            preparedStatement.setString(1, token);
            System.out.println(preparedStatement);
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                log.info("this token don't exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }
}
