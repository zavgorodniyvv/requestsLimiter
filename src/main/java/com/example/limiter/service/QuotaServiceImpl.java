package com.example.limiter.service;

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


}
