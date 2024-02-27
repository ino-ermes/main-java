package com.mio.dao;

import com.mio.entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements GenericDAO<User> {

    @Override
    public long count() {
        long count = 0;
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                Statement statement = connection.createStatement()) {

            String sql = "SELECT COUNT(*) FROM user";

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    count = resultSet.getLong(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public User create(User user) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO user (username, password, casual_name, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getCasualName());
            preparedStatement.setTimestamp(4, new Timestamp(user.getCreatedAt().getTime()));
            preparedStatement.setTimestamp(5, new Timestamp(user.getUpdatedAt().getTime()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating User failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating User failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void delete(Object id) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {

            preparedStatement.setInt(1, (int) id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User get(Object id) {
        User user = null;
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {

            preparedStatement.setInt(1, (int) id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = mapResultSetToUser(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> listAll() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM user";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    userList.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public User update(User user) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE user SET username = ?, password = ?, casual_name = ?, updated_at = ? WHERE id = ?")) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getCasualName());
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("casual_name"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at"));
    }
}
