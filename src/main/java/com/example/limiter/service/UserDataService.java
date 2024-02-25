package com.example.limiter.service;

import com.example.limiter.model.User;

import java.util.Map;

public interface UserDataService {
    User getUserById(String userId);

    User createUser(User user);

    User updateUser(String userId, User updatedUser);

    User deleteUsers(String userId);

    Map<String, Integer> getAllUsers();
}
