package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
public class QuotaServiceImpl implements QuotaService{
    private static final Logger logger = LoggerFactory.getLogger(QuotaServiceImpl.class);
    @Value("${quota}")
    private Integer quota;

    private final UserService userService;
    private final ValidationService validationService;

    public QuotaServiceImpl(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }


    @Override
    public Integer getConsumerQuota(String userId) {
        validationService.validateUserId(userId);
        logger.info("Getting quota for user: {}", userId);
        return userService.getUser(userId).getQuota();
    }

    @Override
    public Map<String, Integer> getUsersQuota() {
        logger.info("Getting all quotas");
        return userService.getAllQuota();
    }

    @Override
    public boolean isRequestPermitted(String userId) {
        validationService.validateUserId(userId);
        User user = userService.getUser(userId);
        updateQuotaAndCheckBlock(user);
        return true;
    }

    private void updateQuotaAndCheckBlock(User user) {
        if(user == null){
            throw new LimiterException("User not found");
        }
        var newQuota = user.getQuota() + 1;
        if(newQuota > quota){
            blockUser(user, newQuota);
            throw new LimiterException("Quota was reached. User is blocked");
        }
        updateUserQuota(user, newQuota);
    }

    private void blockUser(User user, int newQuota) {
        user.setBlocked(true);
        user.setQuota(newQuota);
        user.setLastLoginTimeUtc(TimeUtils.getCurrentDateTimeUTC());
        userService.updateUser(user.getId(), user);
    }

    private void updateUserQuota(User user, int newQuota) {
        user.setQuota(newQuota);
        user.setLastLoginTimeUtc(TimeUtils.getCurrentDateTimeUTC());
        userService.updateUser(user.getId(), user);
    }
}