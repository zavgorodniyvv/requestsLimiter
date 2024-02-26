package com.example.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class DevLimiterApplication {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                .withDatabaseName("userDataBase")
                .withPassword("my-secret-pw")
                .withUsername("root")
                .withInitScript("init.sql")
                .withReuse(false);
    }

    public static void main(String[] args) {
        SpringApplication.from(LimiterApplication::main).with(DevLimiterApplication.class).run(args);
    }

}
