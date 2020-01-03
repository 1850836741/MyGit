package com.example.business_server.safelock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
public class LinkZookeeper {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CuratorFramework getCuratorFramework(){
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(ZK_ADDRESS,new RetryNTimes(10,5000));
        zkClient.start();
        return zkClient;
    }

}