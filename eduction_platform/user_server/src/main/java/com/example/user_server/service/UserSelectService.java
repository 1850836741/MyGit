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
                bloomFilter.setInBloomSet(user_id);
                redisTool.addObjectWithLimit(String.valueOf(user_id),user, MathTool.getRandom(3,8),TimeUnit.HOURS);
            }
            return user;
        }
        return null;
    }




    /**
     * 设置用户登陆
     * @param user_id
     */
    public void setUserLogInDays(int user_id){
        StringBuilder stringBuilder = new StringBuilder(RedisCacheConfig.USER_LOGIN_DAYS_PREFIX);
        stringBuilder.append(user_id);
        redisTool.setBit(stringBuilder.toString(), DateTool.getDaysApartNowYearFirstDay(),true);
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
     *设置用户的等级(将short转为对应配置文件的String)
     * @param user_id
     * @param grade
     */
    public void addUserGrade(int user_id,short grade){
        stringRedisTemplate.opsForHash().put(RedisCacheConfig.USERS_GRADE_HASH,user_id,RedisCacheConfig.getGrade_Map().get(grade));
    }

    /**
     * 获取用户的等级(将short转为对应配置文件的String)
     * @param user_id
     * @return
     */
    public String getUserGrade(int user_id){
        return (String) stringRedisTemplate.opsForHash().get(RedisCacheConfig.USERS_GRADE_HASH,user_id);
    }

}
