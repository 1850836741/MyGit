package com.example.user_server.r_cache.user_cache;


import com.example.user_server.entity.User;
import com.example.user_server.r_cache.RedisTool;
import com.example.user_server.tool.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCacheTool {

    @Autowired
    RedisTool<User> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    public User getUserByIdFromCache(int user_id){
        if (bloomFilter.isExist(user_id)){

        }
        redisTool.selectObject(String.valueOf(user_id),User.class);
        return null;
    }
}
