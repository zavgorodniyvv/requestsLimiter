package com.example.limiter.dao;

import com.example.limiter.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserMySQLRepository extends CrudRepository<User, String> {
}
