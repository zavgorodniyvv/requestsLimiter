package com.example.limiter.dao;

import com.example.limiter.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserElasticSearchRepository extends ElasticsearchRepository<User, String>{

    default Optional<User> findById(String userId) {
        System.out.println("Getting user from ElasticSearch");
        return Optional.of(new User());
    }

    default User save(User user) {
        System.out.println("Saving user to ElasticSearch");
        return user;
    }

    default void delete(User user) {
        System.out.println("Deleting user from ElasticSearch");
    }
}
