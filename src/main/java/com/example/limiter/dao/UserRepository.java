package com.example.limiter.dao;

import com.example.limiter.model.User;

public interface UserRepository {
    User getUserById(String userId);

    User createUser(User user);

    User updateUser(String userId, User updatedUser);

    User deleteUsers(String userId);
}
