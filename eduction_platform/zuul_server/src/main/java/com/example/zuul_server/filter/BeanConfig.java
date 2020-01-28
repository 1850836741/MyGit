package com.example.zuul_server.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置bean依赖的配置文件
 */
@Configuration
public class BeanConfig {

    @Bean
    public ConcurrentHashMap<String, String> getConcurrentHashMap(){
        return new ConcurrentHashMap<>();
    }
}
