package com.example.user_server.service;

import com.example.user_server.entity.User;
import com.example.user_server.mapper.UserMapper;
import com.example.user_server.r_cache.RedisCacheConfig;
import com.example.user_server.r_cache.RedisTool;
import com.example.user_server.tool.BloomFilter;
import com.example.user_server.tool.DateTool;
import com.example.user_server.tool.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息删改查的service
 */
@Service
public class UserChangeService {

    @Autowired
    RedisTool<User> redisTool;

    @Autowired(required = false)
    UserMapper userMapper;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserSelectService userSelectService;

    /**
     * 存储User的基本信息
     * @param user
     */
    public void addUser(User user){
        if (user == null) return;
        bloomFilter.setInBloomSet(user.getUser_id());
        redisTool.deleteCacheByKey(String.valueOf(user.getUser_id()));
        userMapper.addUserBaseInformation(user);
    }

    /**
     * 删除用户
     * @param user
     */
    public void deleteUser(User user){
        if (!userSelectService.isRightfulSizeUserId(user.getUser_id()))return;
        redisTool.deleteCacheByKey(String.valueOf(user.getUser_id()));
        userMapper.deleteUser(user.getUser_id());
    }

    /**
     * 修改用户信息
     * @param user
     */
    public void updateUser(User user){
        if (user == null) return;
        redisTool.deleteCacheByKey(String.valueOf(user.getUser_id()));
        userMapper.updateUser(user);
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
     * 设置今日用户登陆
     * @param user_id
     */
    public void setUserLogInDays(int user_id){
        StringBuilder stringBuilder = new StringBuilder(RedisCacheConfig.USER_LOGIN_DAYS_PREFIX);
        stringBuilder.append(user_id);
        redisTool.setBit(stringBuilder.toString(), DateTool.getDaysApartNowYearFirstDay(),true);
    }

    /**
     * 用户注册
     * @param user
     */
    public void registerUser(User user, HttpServletResponse httpServletResponse){
        if (userSelectService.isRightfulSizeUserId(user.getUser_id()) && userSelectService.isRightfulSizeUserPassword(user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setCreat_time(DateTool.getTime());
            Map<String,String> uriVariables = new HashMap<>(2);
            uriVariables.put("username",String.valueOf(user.getUser_id()));
            uriVariables.put("password",user.getPassword());

            /*向oauth2登陆中心缓存存入账户信息*/
            restTemplate.postForObject(RedisCacheConfig.ZUUL_SERVER_PREFIX
                    .concat("oauth2/addUserToCache"),user,User.class);

            /*向oauth2登陆中心发起登陆获取token请求*/
            String result = restTemplate.exchange(
                    RedisCacheConfig.ZUUL_SERVER_PREFIX
                            .concat("oauth2/oauth/token?grant_type=password&client_id=client&client_secret=secret&username="
                                            .concat(String.valueOf(user.getUser_id()))
                                            .concat("&password=").concat(user.getPassword()))
                    , HttpMethod.GET, null, String.class, uriVariables).getBody();


            /*将获取到的token和refreshToken放入cookie中*/
            Cookie cookie = new Cookie(RedisCacheConfig.COOKIE_TOKEN_KEY, StringTool.getWordWithJSON(result,RedisCacheConfig.COOKIE_TOKEN_KEY));
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            cookie = new Cookie(RedisCacheConfig.COOKIE_REFRESH_TOKEN_KEY, StringTool.getWordWithJSON(result,RedisCacheConfig.COOKIE_REFRESH_TOKEN_KEY));
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);

            /*异步添加用户信息到数据库*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addUser(user);
                }
            }).start();
        }
    }
}
