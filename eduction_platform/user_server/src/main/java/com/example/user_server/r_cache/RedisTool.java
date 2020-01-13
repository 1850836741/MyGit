package com.example.user_server.r_cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 操作redis缓存的工具类
 */
public class RedisTool {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /*向缓存中添加key-value*/
    public void addObject(Object object){

    }
}
