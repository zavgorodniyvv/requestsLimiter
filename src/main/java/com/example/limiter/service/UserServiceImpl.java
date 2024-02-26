package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ValidationService validationService;
    private final DataSourceRouter dataSourceRouter;

    public UserServiceImpl(ValidationService validationService, DataSourceRouter dataSourceRouter) {
        this.validationService = validationService;
        this.dataSourceRouter = dataSourceRouter;
    }


    @Override
    public User getUser(String userId) {
        logger.info("Got request \"Get User\", userId = {}", userId);
        validationService.validateUserId(userId);
        return dataSourceRouter.getRepository().findById(userId).orElseThrow(() -> new LimiterException("User not found"));
    }

    @Override
    public User createUser(User user) {
        logger.info("Got request \"Create User\"");
        logger.debug("User: {}", user);
        validationService.validateUser(user);
        user.setLastLoginTimeUtc(TimeUtils.getCurrentDateTimeUTC());
        user = dataSourceRouter.getRepository().save(user);
        logger.info("User created successfully");
        logger.debug("User: {}", user);
        return user;
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        logger.info("Got request \"Update User\", userId = {}", userId);
        logger.debug("User: {}", updatedUser);
        validationService.validateUserIdAndUser(userId, updatedUser);
        updatedUser.setId(userId);
        updatedUser.setLastLoginTimeUtc(TimeUtils.getCurrentDateTimeUTC());
        User user = dataSourceRouter.getRepository().save(updatedUser);
        validationService.validateUser(user);
        logger.info("User with ID {} updated successfully", userId);
        logger.debug("User: {}", user);
        return user;
    }

    @Override
    public User deleteUser(String userId) {
        logger.info("Got request \"Delete User\", userId = {}", userId);
        validationService.validateUserId(userId);
        User user = getUser(userId);
        dataSourceRouter.getRepository().delete(user);
        logger.info("User with ID {} deleted successfully", userId);
        return user;
    }
    @Override
    public Map<String, Integer> getAllQuota() {
        logger.info("Got request \"Get All Quota\"");
        Iterable<User> users = dataSourceRouter.getRepository().findAll();
        if(!users.iterator().hasNext()){
            throw new LimiterException("No users found");
        }
        return StreamSupport.stream(users.spliterator(), false).collect(Collectors.toMap((User::getFullName), User::getRequestsNumber));
    }


}
