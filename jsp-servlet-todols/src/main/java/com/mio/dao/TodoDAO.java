package com.mio.dao;

import com.mio.entity.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO implements GenericDAO<Todo> {

    @Override
    public long count() {
        long count = 0;
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                Statement statement = connection.createStatement()) {

            String sql = "SELECT COUNT(*) FROM todo";

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
    public Todo create(Todo todo) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO todo (user_id, task_name, deadline, is_completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, todo.getUserId());
            preparedStatement.setString(2, todo.getTaskName());
            preparedStatement.setDate(3, new java.sql.Date(todo.getDeadline().getTime()));
            preparedStatement.setBoolean(4, todo.getIsCompleted());
            preparedStatement.setTimestamp(5, new Timestamp(todo.getCreatedAt().getTime()));
            preparedStatement.setTimestamp(6, new Timestamp(todo.getUpdatedAt().getTime()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Todo failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    todo.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Todo failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    @Override
    public void delete(Object id) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM todo WHERE id = ?")) {

            preparedStatement.setInt(1, (int) id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Todo get(Object id) {
        Todo todo = null;
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM todo WHERE id = ?")) {

            preparedStatement.setInt(1, (int) id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    todo = mapResultSetToTodo(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todo;
    }

    @Override
    public List<Todo> listAll() {
        List<Todo> todoList = new ArrayList<>();
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM todo";

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Todo todo = mapResultSetToTodo(resultSet);
                    todoList.add(todo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todoList;
    }

    @Override
    public Todo update(Todo todo) {
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE todo SET user_id = ?, task_name = ?, deadline = ?, is_completed = ?, updated_at = ? WHERE id = ?")) {

            preparedStatement.setInt(1, todo.getUserId());
            preparedStatement.setString(2, todo.getTaskName());
            preparedStatement.setDate(3, new java.sql.Date(todo.getDeadline().getTime()));
            preparedStatement.setBoolean(4, todo.getIsCompleted());
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(6, todo.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    private Todo mapResultSetToTodo(ResultSet resultSet) throws SQLException {
        return new Todo(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("task_name"),
                resultSet.getDate("deadline"),
                resultSet.getBoolean("is_completed"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at"));
    }
}
