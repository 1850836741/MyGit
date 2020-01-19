package com.example.user_server;

import com.example.user_server.entity.User;
import com.example.user_server.tool.UnsafeTool;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class simpleTest {

    @Test
    public void unsafeTest(){
        User user = new User();
        user.setUser_id(1);
        user.setName("QAQ");
        System.out.println("==="+user.getName()+"===");
        try {
            UnsafeTool.setObject(user,"name","QAQ","QWQ");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println("==="+user.getName()+"===");
    }

    @Test
    public void atomicTest(){
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread t = null;
        for (int i = 0;i<1000;i++){
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int x = atomicInteger.get();
                    while (!atomicInteger.compareAndSet(x,x+1)){
                        x = atomicInteger.get();
                    }
                    System.out.println(x+1);
                }
            });
            t.start();
        }
    }

    @Test
    public void timeTest(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        System.out.println(simpleDateFormat.format(date));
        System.out.println(Integer.parseInt("01"));
    }

}
