package com.mio.service;

import java.util.List;

import com.mio.entity.*;

public interface ITodoService {
    List<Todo> getAllByUserId(int userId);
    Todo create(Todo todo);
    Todo updateById(int id, Todo todo);
    Todo completeTodoById(int id);
    void deleteById(int id);
}
