package com.example.user_server.controller;

import com.example.user_server.entity.User;
import com.example.user_server.service.UserChangeService;
import com.example.user_server.service.UserSelectService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 有关用户信息查询方面的控制层
 */
@RestController
@DefaultProperties(commandProperties = {
        /*设置断路器超时时间，单位为毫秒,可通过HystrixCommandProperties类查找*/
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
},defaultFallback = "fallbackMode"
        /*创建舱壁模式,设置最大线程数,以及最大等待队列*/
        , threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "30"),
        @HystrixProperty(name = "maxQueueSize", value = "10")
}, threadPoolKey = "UserSelectPool")
public class UserSelectController {

    @Autowired
    UserSelectService userSelectService;

    @Autowired
    UserChangeService userChangeService;

    /**
     * 获取用户全部信息
     * @param user_id
     * @return
     */
    @HystrixCommand()
    @RequestMapping(value = "/getUserAllInformation")
    public User getUserAllInformation(int user_id){
        return userSelectService.getUserByIdFromCache(user_id);
    }

    /**
     * 获取用户等级
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/getUserGrade")
    public String getUserGrade(int user_id){
        return userSelectService.getUserGrade(user_id);
    }

    /**
     * 获取用户今年登陆天数
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/getUserLogInDays")
    public long getUserLogInDays(int user_id){
        return userSelectService.getUserLogInDays(user_id);
    }

    /**
     * 熔断器超时的后备方法
     * @return
     */
    public String fallbackMode(){
        return "出了点小问题,请稍后再试";
    }
}
