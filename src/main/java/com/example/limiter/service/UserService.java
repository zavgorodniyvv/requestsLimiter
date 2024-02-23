package com.example.limiter.service;

import com.example.limiter.model.User;
import org.springframework.http.HttpStatusCode;

public interface UserService {
    User getUser(String userId);

    User createUser(User user);

    User updateUser(String userId, User updatedUser);

    User deleteUser(String userId);

}
