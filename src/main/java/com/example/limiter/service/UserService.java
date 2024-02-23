package com.example.limiter.service;

import com.example.limiter.model.User;

public interface UserService {
    User getUser(String userId);

    User createUser(User user);

    User updateUser(String userId, User updatedUser);

    User deleteUser(String userId);

}
