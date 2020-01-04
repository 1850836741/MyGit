package com.example.oauth2_server;

<<<<<<< HEAD
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
=======
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
<<<<<<< HEAD
@EnableDiscoveryClient
@MapperScan(value = "com.example.oauth2_server.mapper")
=======
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
public class Oauth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }

}
