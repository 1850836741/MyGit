package com.example.user_server;

import com.example.user_server.bean.User;
import com.example.user_server.r_cache.RedisCacheConfig;
import com.example.user_server.tool.UnsafeTool;
import org.junit.Test;

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

}
