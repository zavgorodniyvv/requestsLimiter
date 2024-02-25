package com.example.limiter.dao;

import com.example.limiter.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl implements UserRepository{
    private final UserMySQLRepository mySQLRepository;

    public UserRepositoryImpl(UserMySQLRepository mySQLRepository) {
        this.mySQLRepository = mySQLRepository;
    }

    @Override
    public User getUserById(String userId) {
        return mySQLRepository.getReferenceById(userId);
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        return null;
    }

    @Override
    public User deleteUsers(String userId) {
        return null;
    }
}
