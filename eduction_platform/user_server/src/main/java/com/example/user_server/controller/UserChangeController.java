package com.example.user_server.controller;

import com.example.user_server.entity.User;
import com.example.user_server.r_cache.RedisCacheConfig;
import com.example.user_server.service.UserChangeService;
import com.example.user_server.service.UserSelectService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@DefaultProperties(commandProperties = {
        /*设置断路器超时时间，单位为毫秒,可通过HystrixCommandProperties类查找*/
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
},defaultFallback = "fallbackMode"
        /*创建舱壁模式,设置最大线程数,以及最大等待队列*/
        , threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "30"),
        @HystrixProperty(name = "maxQueueSize", value = "10")
}, threadPoolKey = "UserChangePool")
public class UserChangeController {

    @Autowired
    UserChangeService userChangeService;

    @Autowired
    UserSelectService userSelectService;

    /**
     * 注册用户控制层
     * @param httpServletResponse
     * @param user
     * @return
     */
    @RequestMapping(value = "/registerUser")
    public boolean registerUser(HttpServletResponse httpServletResponse, User user){
        userChangeService.registerUser(user, httpServletResponse);
        return true;
    }

    /**
     * 修改用户控制层
     * @param user
     * @return
     */
    public User updateUser(User user){
        userChangeService.updateUser(user);
        return user;
    }

    /**
     * 删除用户
     * @param user
     */
    public void deleteUser(User user){
        if (userSelectService.isRightUser(user)){
            userChangeService.deleteUser(user);
        }
    }

    /**
     * 注销用户
     * @param httpServletRequest
     * @param httpServletResponse
     */
    public void loginOut(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        /*初始化session*/
        httpServletRequest.getSession().invalidate();

        /*删除存储在cookie里的token和refreshToken*/
        Cookie cookie = new Cookie(RedisCacheConfig.COOKIE_TOKEN_KEY,"");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        cookie = new Cookie(RedisCacheConfig.COOKIE_REFRESH_TOKEN_KEY,"");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
