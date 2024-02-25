package com.example.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.limiter.dao")
public class LimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimiterApplication.class, args);
    }

}
