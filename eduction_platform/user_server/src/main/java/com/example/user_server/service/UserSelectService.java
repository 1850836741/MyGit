package com.example.user_server.service;


import com.example.user_server.entity.User;
import com.example.user_server.mapper.UserMapper;
import com.example.user_server.r_cache.RedisCacheConfig;
import com.example.user_server.r_cache.RedisTool;
import com.example.user_server.tool.BloomFilter;
import com.example.user_server.tool.DateTool;
import com.example.user_server.tool.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * User关于查询的服务
 */
@Service
public class UserSelectService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTool<User> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired(required = false)
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
     * 获取今年用户登录总天数
     * @param user_id
     * @return
     */
    public Long getUserLogInDays(int user_id){
        StringBuilder stringBuilder = new StringBuilder(RedisCacheConfig.USER_LOGIN_DAYS_PREFIX);
        stringBuilder.append(user_id);
        return redisTool.getBitCount(stringBuilder.toString(),DateTool.getDaysApartNowYearFirstDay());
    }


    /**
     * 获取用户的等级(将short转为对应配置文件的String)
     * @param user_id
     * @return
     */
    public String getUserGrade(int user_id){
        return (String) stringRedisTemplate.opsForHash().get(RedisCacheConfig.USERS_GRADE_HASH,user_id);
    }

    /**
     * 判断账号大小是否符合
     * @param user_id
     * @return
     */
    public  boolean isRightfulSizeUserId(int user_id){
        return user_id >= RedisCacheConfig.RIGHTFUL_USER_ID_MIN && user_id <= RedisCacheConfig.RIGHTFUL_USER_ID_MAX;
    }

    /**
     * 判断密码长度是否符合
     * @param password
     * @return
     */
    public boolean isRightfulSizeUserPassword(String password){
        return password.length() <= RedisCacheConfig.RIGHTFUL_USER_PASSWORD_SIZE_MAX
                && password.length() >= RedisCacheConfig.RIGHTFUL_USER_PASSWORD_SIZE_MIN;
    }

    /**
     * 判断是否为合法用户
     * @param user
     * @return
     */
    public boolean isRightUser(User user){
        User realUser = getUserByIdFromCache(user.getUser_id());
        return realUser != null && realUser.getPassword().equals(user.getPassword());
    }
}
