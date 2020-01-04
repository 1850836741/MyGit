package com.example.zuul_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
=======
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
<<<<<<< HEAD
@EnableDiscoveryClient
=======
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
public class ZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }

}
