package com.mio.service;

import java.util.List;

import com.mio.entity.Todo;

public class TodoService implements ITodoService{
    private TodoService() {

    }

    private static TodoService instance;

    static {
        instance = null;
    }

    public static TodoService getInstance() {
        if (instance == null) {
            instance = new TodoService();
        }
        return instance;
    }
    @Override
    public Todo completeTodoById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Todo create(Todo todo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Todo> getAllByUserId(int userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Todo updateById(int id, Todo todo) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
