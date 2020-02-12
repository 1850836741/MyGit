package com.example.material_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class BeanConfig {

    /**
     * 加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 线程池
     * @return
     */
    @Bean
    public Executor executor(){
        return Executors.newFixedThreadPool(6);
    }

    /**
     * 用以消息通知
     * @return
     */
    @Bean
    public ConcurrentHashMap<String,String> concurrentHashMap(){
        return new ConcurrentHashMap<String,String>();
    }
}
