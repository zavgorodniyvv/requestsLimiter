package com.example.limiter.service;

import com.example.limiter.dao.UserRepository;
import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userId) {
        logger.info("Got request \"Get User\", userId = {}", userId);
        if(userId == null || userId.isBlank()){
            throw new LimiterException("UserId  is null or empty");
        }
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new LimiterException("User not found");
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        logger.info("Got request \"Create User\"");
        logger.debug("User: {}", user);
        if(user == null){
            throw new LimiterException("Could not create user. User from request is null");
        }
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        logger.info("Got request \"Update User\", userId = {}", userId);
        logger.debug("User: {}", updatedUser);
        User user = userRepository.updateUser(userId, updatedUser);
        if (user == null) {
            throw new LimiterException("Could not update user");
        }
        return user;
    }

    @Override
    public User deleteUser(String userId) {
        logger.info("Got request \"Delete User\", userId = {}", userId);
        if(userId == null || userId.isBlank()){
            throw new LimiterException("UserId  is null or empty");
        }
        User user = userRepository.deleteUsers(userId);
        if (user == null) {
            throw new LimiterException("Could not delete User");
        }
        return user;
    }
}
