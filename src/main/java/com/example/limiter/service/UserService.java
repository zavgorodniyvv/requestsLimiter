package com.example.limiter.service;

import com.example.limiter.model.User;

import java.util.Map;

public interface UserService {
    User getUser(String userId);

    User createUser(User user);

    User updateUser(String userId, User updatedUser);

    User deleteUser(String userId);

    Map<String, Integer> getAllQuota();
}
