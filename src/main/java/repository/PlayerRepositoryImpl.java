package repository;

import config.ConnectionUtils;
import model.Player;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRepositoryImpl implements PlayerRepository {


    @Override
    public void save(Player inputPlayer) throws SQLException {
        String insertDataSQL = "INSERT INTO wallet.\"players-liquibase\" (username, password, account) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setString(1, inputPlayer.getName());
            preparedStatement.setString(2, inputPlayer.getPassword());
            preparedStatement.setInt(3, 0);
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
    public Player findById(long id) throws SQLException {
        String insertDataSQL = "select * from wallet.\"players-liquibase\" where id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            String name = null, password = null;
            long account = 0, idResult = 0;
            if (resultSet.next()) {
                idResult = resultSet.getLong("id");
                name = resultSet.getString("username");
                password = resultSet.getString("password");
                account = resultSet.getLong("account");
            } else {
                System.out.println("Record not found.");
            }
            return new Player(idResult, name, password, account);

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
    public Player findByNamePassword(String name, String password) throws SQLException {
        String insertDataSQL = "select * from wallet.\"players-liquibase\" where username=? and \"password\"=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            String nameResult = null, passwordResult = null;
            long accountResult = 0, idResult = 0;
            if (resultSet.next()) {
                idResult = resultSet.getLong("id");
                nameResult = resultSet.getString("username");
                passwordResult = resultSet.getString("password");
                accountResult = resultSet.getLong("account");
            } else {
                System.out.println("Record not found.");
            }
            return new Player(idResult, nameResult, passwordResult, accountResult);
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
    public Player update(Player updatePlayer) throws SQLException {
        String insertDataSQL = "UPDATE wallet.\"players-liquibase\" SET account = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertDataSQL);
            preparedStatement.setLong(1, updatePlayer.getAccount());
            preparedStatement.setLong(2, updatePlayer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
        return findById(updatePlayer.getId());
    }
}
