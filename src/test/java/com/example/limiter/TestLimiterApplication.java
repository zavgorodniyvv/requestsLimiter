package com.example.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestLimiterApplication {

    @Bean
    @ServiceConnection
    ElasticsearchContainer elasticsearchContainer() {
        return new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10"));
    }

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(LimiterApplication::main).with(TestLimiterApplication.class).run(args);
    }

}