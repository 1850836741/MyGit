package com.example.business_server;

<<<<<<< HEAD
import com.example.business_server.entity.Goods;
import com.example.business_server.entity.Source;
import com.example.business_server.service.GoodsService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.business_server.mapper")
public class BusinessServerApplication {

=======
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BusinessServerApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    public static void main(String[] args) {
        SpringApplication.run(BusinessServerApplication.class, args);
    }

}
