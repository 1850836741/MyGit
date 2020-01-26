package com.example.oauth2_server.config.entity;

import com.example.oauth2_server.entity.UserLoginIpAndSessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听所有session
 */
public class SessionListen implements HttpSessionListener {

    @Autowired
    ConcurrentHashMap<String, UserLoginIpAndSessionId> concurrentHashMap;

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}
