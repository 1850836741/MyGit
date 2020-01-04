package com.example.customer_server;

import com.example.customer_server.service.safe.LockConfig;
import com.example.customer_server.service.safe.ZookeeperService;

import java.util.UUID;

public class Test {

    ZookeeperService zookeeperService = new ZookeeperService();

    @org.junit.Test
    public void LockTest(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }
}
