package com.example.resources_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@MapperScan(value = "com.example.resources_server.mapper")
public class ResourcesServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourcesServerApplication.class, args);
    }

}
