package com.example.resources_server.zk_safelock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 连接Zookeeper服务端
 */
@Configuration
public class LinkZookeeper {
    /*绑定的地址ip地址及端口号(2181为默认端口)*/
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    /*返回一个连接实例*/
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CuratorFramework getCuratorFramework(){
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(ZK_ADDRESS,new RetryNTimes(10,5000));
        zkClient.start();
        return zkClient;
    }

}