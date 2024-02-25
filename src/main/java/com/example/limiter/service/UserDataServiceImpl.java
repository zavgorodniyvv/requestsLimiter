package com.example.limiter.service;

import com.example.limiter.dao.UserElasticSearchRepository;
import com.example.limiter.dao.UserMySQLRepository;
import com.example.limiter.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class UserDataServiceImpl implements UserDataService{
    private final UserMySQLRepository mySQLRepository;
    private final UserElasticSearchRepository elasticSearchRepository;

    public UserDataServiceImpl(UserMySQLRepository mySQLRepository, UserElasticSearchRepository elasticSearchRepository) {
        this.mySQLRepository = mySQLRepository;
        this.elasticSearchRepository = elasticSearchRepository;
    }

    @Override
    public User getUserById(String userId) {
        return getRepository().findById(userId).orElse(null);
    }

    @Override
    public User createUser(User user) {
        return getRepository().save(user);
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        updatedUser.setId(userId);
        return getRepository().save(updatedUser);
    }

    @Override
    public User deleteUsers(String userId) {
        User user = getUserById(userId);
        getRepository().delete(user);
        return user;
    }

    @Override
    public Map<String, Integer> getAllUsers() {
        Iterable<User> users = getRepository().findAll();
        return StreamSupport.stream(users.spliterator(), false).collect(Collectors.toMap((User::getFullName), User::getQuota));
    }

    private CrudRepository<User, String> getRepository() {
        LocalTime currentTimeUTC = LocalTime.now(ZoneOffset.UTC);
        LocalTime mySQLWorkTimeStart = LocalTime.of(9, 0);
        LocalTime mySQLWorkTimeEnd = LocalTime.of(17, 0);

        if(currentTimeUTC.isAfter(mySQLWorkTimeStart) && currentTimeUTC.isBefore(mySQLWorkTimeEnd)) {
            return mySQLRepository;
        }
        return elasticSearchRepository;
    }
}