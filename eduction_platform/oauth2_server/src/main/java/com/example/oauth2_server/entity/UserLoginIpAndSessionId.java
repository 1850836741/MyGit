package com.example.oauth2_server.entity;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Data
@Component
public class UserLoginIpAndSessionId {
    private String ip;
    private HttpSession session;

    public UserLoginIpAndSessionId(){
    }

    public UserLoginIpAndSessionId(String ip, HttpSession session){
        this.ip = ip;
        this.session = session;
    }
}
