package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuotaServiceImpl implements QuotaService{
    @Value("${quota}")
    private Integer quota;

    private final UserDataService userDataService;

    public QuotaServiceImpl(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @Override
    public Integer getConsumerQuota(String userId) {
        User user = userDataService.getUserById(userId);
        return user.getQuota();
    }

    @Override
    public Map<String, Integer> getUsersQuota() {
        return userDataService.getAllUsers();
    }

    @Override
    public boolean isRequestPermitted(String userId) {
        if(userId == null || userId.isBlank()){
            throw new LimiterException("User id is required");
        }
        User user = userDataService.getUserById(userId);
        if(user.isBlocked()){
            throw new LimiterException("User is blocked");
        }
        var newQuota = user.getQuota() + 1;
        if(newQuota > quota){
           user.setBlocked(true);
           user.setQuota(newQuota);
           userDataService.updateUser(userId, user);
              throw new LimiterException("Quota was reached. User is blocked");
        }
        user.setQuota(newQuota);
        userDataService.updateUser(userId, user);
        return true;
    }


}
