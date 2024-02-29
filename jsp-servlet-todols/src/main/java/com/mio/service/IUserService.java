package com.mio.service;

import com.mio.entity.User;

public interface IUserService {
    User checkLogin(String username, String password);
    User createUser(User user);
}
