package com.example.oauth2_server.r_cache.user_cache;

import com.example.oauth2_server.entity.User;
import com.example.oauth2_server.mapper.UserMapper;
import com.example.oauth2_server.r_cache.RedisCacheConfig;
import com.example.oauth2_server.r_cache.RedisTool;
import com.example.oauth2_server.tool.BloomFilter;
import com.example.oauth2_server.tool.DateTool;
import com.example.oauth2_server.tool.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * User缓存工具包
 */
@Component
public class UserCacheTool extends RedisTool{

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTool<User> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired(required = false)
    UserMapper userMapper;


    /***
     * 查询出登陆所需要的账号，密码，权限信息,解决缓存穿透，雪崩等问题
     * @param user_id
     * @return
     */
    public User getUserByIdFromCache(int user_id){
        if (bloomFilter.isExist(user_id)){
            if (stringRedisTemplate.opsForValue().get(user_id).equals("empty"))return null;
            User user = redisTool.selectObject(String.valueOf(user_id),User.class);
            if (user == null){
                user = userMapper.getLogInInformation(user_id);
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
     * 向缓存中添加User
     * @param user
     */
    public void addUser(User user){
        redisTool.addObject(String.valueOf(user.getUser_id()),user);
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
     * 记录用户登录个数
     * @param user_id
     */
    public void setUserLoginToCount(int user_id){
        redisTool.addHypeLogLog(RedisCacheConfig.ALL_USER_LOGIN_NUMBER_KEY, String.valueOf(user_id));
    }
}
