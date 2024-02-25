package com.example.limiter.service;

import com.example.limiter.model.User;
import org.springframework.data.repository.CrudRepository;

public interface DataSourceRouter {
    CrudRepository<User, String> route();
}
