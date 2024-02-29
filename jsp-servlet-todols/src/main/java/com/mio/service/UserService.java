package com.mio.service;

import com.mio.entity.User;

public class UserService implements IUserService{

    private UserService() {

    }

    private static UserService instance;

    static {
        instance = null;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    @Override
    public User checkLogin(String username, String password) {
        return new User(0, "Kino", "password", null, null, null);
    }

    @Override
    public User createUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }
    
}