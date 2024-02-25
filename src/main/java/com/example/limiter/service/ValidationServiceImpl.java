package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService{
    @Override
    public void validateUserId(String userId){
        if(userId == null || userId.isBlank()){
            throw new LimiterException("UserId is null or empty");
        }
    }

    @Override
    public void validateUser(User user){
        if(user == null){
            throw new LimiterException("User is null");
        }
        if(user.getFirstName() == null || user.getFirstName().isBlank()){
            throw new LimiterException("First name is null or empty");
        }
        if(user.getLastName() == null || user.getLastName().isBlank()){
            throw new LimiterException("Last name is null or empty");
        }
    }

    @Override
    public void validateUserIdAndUser(String userId, User user){
        validateUserId(userId);
        validateUser(user);
    }
}
