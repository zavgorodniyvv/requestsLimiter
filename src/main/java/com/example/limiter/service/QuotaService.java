package com.example.limiter.service;

import java.util.Map;

public interface QuotaService {
    Integer getConsumerQuota(String userId);

    Map<String, Integer> getUsersQuota();
}
