package com.example.oauth2_server.config.entity;


import com.example.oauth2_server.tool.IpAdressTool;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;



/**
 * 认证成功后触发事件
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        if (authenticationSuccessEvent.getSource().getClass().getName().equals("org.springframework.security.authentication.UsernamePasswordAuthenticationToken")){
            System.out.println(authenticationSuccessEvent.getAuthentication().getName());
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            System.out.println(IpAdressTool.getIpAddress(request));
            System.out.println(request.getRequestedSessionId());
        }
    }
}
