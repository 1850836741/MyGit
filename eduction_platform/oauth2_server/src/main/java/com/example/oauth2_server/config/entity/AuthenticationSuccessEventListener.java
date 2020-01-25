package com.example.oauth2_server.config.entity;


import com.example.oauth2_server.entity.UserLoginIpAndSessionId;
import com.example.oauth2_server.r_cache.user_cache.UserCacheTool;
import com.example.oauth2_server.tool.IpAdressTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证成功后触发事件
 */
@Component
@Configuration
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserCacheTool userCacheTool;

    @Autowired
    UserLoginIpAndSessionId userLoginIpAndSessionId;

    @Bean
    public ConcurrentHashMap<String,UserLoginIpAndSessionId> getUserLoginIpAndSessionIdConcurrentHashMap(){
        return new ConcurrentHashMap<>();
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        //若是登陆成功事件则执行
        if (authenticationSuccessEvent.getSource().getClass().getName().equals("org.springframework.security.authentication.UsernamePasswordAuthenticationToken")){

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            userLoginIpAndSessionId.setIp(IpAdressTool.getIpAddress(request));
            userLoginIpAndSessionId.setSession(request.getSession());

            //如果已经包含了该账号的登陆信息
            if (getUserLoginIpAndSessionIdConcurrentHashMap()
                    .containsKey(authenticationSuccessEvent.getAuthentication().getName())){
                //获取原来登陆对象
                UserLoginIpAndSessionId userLoginIpAndSessionId1 = getUserLoginIpAndSessionIdConcurrentHashMap()
                        .get(authenticationSuccessEvent.getAuthentication().getName());

                //若登陆地点不一致
                if (!userLoginIpAndSessionId1.getIp().equals(userLoginIpAndSessionId.getIp())){
                    userLoginIpAndSessionId1.getSession().invalidate();
                    getUserLoginIpAndSessionIdConcurrentHashMap().put(authenticationSuccessEvent.getAuthentication().getName(),
                            userLoginIpAndSessionId);
                }
            }else {
                //向map中储存
                getUserLoginIpAndSessionIdConcurrentHashMap().put(authenticationSuccessEvent.getAuthentication().getName(),userLoginIpAndSessionId);

                //统计并设置用户登陆状态
                userCacheTool.setUserLoginToCount(Integer.valueOf(authenticationSuccessEvent.getAuthentication().getName()));
                userCacheTool.setUserLogInDays(Integer.valueOf(authenticationSuccessEvent.getAuthentication().getName()));
            }
        }
    }
}
