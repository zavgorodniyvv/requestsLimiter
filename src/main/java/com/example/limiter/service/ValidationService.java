package com.example.limiter.service;

import com.example.limiter.model.User;

public interface ValidationService {

    void validateUserId(String userId);

    void validateUser(User user);

    void validateUserIdAndUser(String userId, User user);
}
