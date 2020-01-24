package com.example.oauth2_server;

import com.example.oauth2_server.tool.IpAdressTool;
import org.junit.Test;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 一些简洁的测试
 */
public class simpleTest {

    @Test
    public void ipTest(){
        try {
            System.out.println(IpAdressTool.getLocalhostIp());
            System.out.println(IpAdressTool.getLocalIpByFirstNetCard());
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }
}
