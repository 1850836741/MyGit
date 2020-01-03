package com.example.logintest.controll;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class T {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public Cookie[] get(HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        return cookies;
    }
}
