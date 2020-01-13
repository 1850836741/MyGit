package com.example.zuul_server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZuulServerApplicationTests {

    @Test
    public void contextLoads() {
        Logger logger = LoggerFactory.getLogger(ZuulServerApplication.class);
        logger.error("哦豁");
        System.out.println("????????????????");
    }
}
