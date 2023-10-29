package org.davydov.repository;

import lombok.RequiredArgsConstructor;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для хранения игрока
 */
@Repository
@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepository {

    private final DBConnectionProvider dbConnectionProvider;
    public static final String UPDATE_PLAYER = """
            UPDATE wallet."player" SET account = ? WHERE id = ?""";
    public static final String INSERT_PLAYER = """
            INSERT INTO wallet."player" ("id", user_name, password, account) 
            VALUES (nextval( 'wallet.sequence_player'), ?, ?, ?)""";
    public static final String SELECT_FIND_BY_ID_PLAYER = """
            select * from wallet."player" where id = ?""";
    public static final String SELECT_FIND_BY_NAME_PASSWORD = """
            select * from wallet."player" where user_name=? and "password"=?""";

    /**
     * Метод сохраняет игрока
     *
     * @param inputPlayer данные игрока
     * @throws SQLException
     */
    @Override
    public void save(Player inputPlayer) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_PLAYER);
            preparedStatement.setString(1, inputPlayer.getName());
            preparedStatement.setString(2, inputPlayer.getPassword());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }

    /**
     * Метод возвращает игрока
     *
     * @param id ID игрока
     * @return игрок
     * @throws SQLException
     */
    @Override
    public Player findById(long id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnectionProvider.getConnection();
            ;
            preparedStatement = connection.prepareStatement(SELECT_FIND_BY_ID_PLAYER);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            String name = null, password = null;
            long account = 0, idResult = 0;
            if (resultSet.next()) {
                idResult = resultSet.getLong("id");
                name = resultSet.getString("user_name");
                password = resultSet.getString("password");
                account = resultSet.getLong("account");

            } else {
                System.out.println("Record not found.");
            }
            return new Player(idResult, name, password, account);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    }

    /**
     * Метод возвращает игрока
     *
     * @param dto игрока
     * @return игрок
     * @throws SQLException
     */
    @Override
    public Player findByNamePassword(PlayerDTO dto) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FIND_BY_NAME_PASSWORD);
            preparedStatement.setString(1, dto.getName());
            preparedStatement.setString(2, dto.getPassword());
            resultSet = preparedStatement.executeQuery();
            String nameResult = null, passwordResult = null;
            long accountResult = 0, idResult = 0;
            if (resultSet.next()) {
                idResult = resultSet.getLong("id");
                nameResult = resultSet.getString("user_name");
                passwordResult = resultSet.getString("password");
                accountResult = resultSet.getLong("account");
            } else {
               throw new SQLException("this player doesn't exist");
            }
            return new Player(idResult, nameResult, passwordResult, accountResult);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    }

    /**
     * Метод обновляет account игрока
     *
     * @param updatePlayer игрок с новым account
     * @return игрока с обновленным account
     * @throws SQLException
     */
    @Override
    public Player update(Player updatePlayer) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            ;
            preparedStatement = connection.prepareStatement(UPDATE_PLAYER);
            preparedStatement.setLong(1, updatePlayer.getAccount());
            preparedStatement.setLong(2, updatePlayer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
            preparedStatement.close();
        }
        return findById(updatePlayer.getId());
    }
}
