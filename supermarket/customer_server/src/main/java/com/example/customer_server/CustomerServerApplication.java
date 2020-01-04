package com.example.customer_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
=======
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
<<<<<<< HEAD
@EnableDiscoveryClient
@MapperScan("com.example.customer_server.mapper")
public class CustomerServerApplication {
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
=======
@EnableEurekaClient
@MapperScan("com.example.customer_server.mapper")
public class CustomerServerApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(CustomerServerApplication.class, args);
    }

}
