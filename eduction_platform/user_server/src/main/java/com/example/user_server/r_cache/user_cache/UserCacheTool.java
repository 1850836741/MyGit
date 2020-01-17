package com.example.user_server.r_cache.user_cache;


import com.example.user_server.entity.User;
import com.example.user_server.mapper.UserMapper;
import com.example.user_server.r_cache.RedisTool;
import com.example.user_server.tool.BloomFilter;
import com.example.user_server.tool.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * User缓存工具包
 */
public class UserCacheTool {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTool<User> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    UserMapper userMapper;

    /***
     * 根据ID查询用户信息,解决缓存穿透，雪崩等问题
     * @param user_id
     * @return
     */
    public User getUserByIdFromCache(int user_id){
        if (bloomFilter.isExist(user_id)){
            if (stringRedisTemplate.opsForValue().get(user_id).equals("empty"))return null;
            User user = redisTool.selectObject(String.valueOf(user_id),User.class);
            if (user == null){
                user = userMapper.getUserById(user_id);
                if (user == null){
                    redisTool.addEmptyWithLimit(String.valueOf(user_id),6, TimeUnit.SECONDS);
                    return null;
                }
                redisTool.addObjectWithLimit(String.valueOf(user_id),user, MathTool.getRandom(3,8),TimeUnit.HOURS);
            }
            return user;
        }
        return null;
    }

    /**
     * 向缓存中添加User
     * @param user
     */
    public void addUser(User user){
        redisTool.addObject(String.valueOf(user.getUser_id()),user);
    }


    public void setUserLogIn(int user_id){
        
    }

}
