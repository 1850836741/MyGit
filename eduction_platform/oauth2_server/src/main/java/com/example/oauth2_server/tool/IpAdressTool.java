package com.example.oauth2_server.tool;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

/**
 * 操纵IP地址的工具类
 */
public class IpAdressTool {
    public final static String LOCALHOST_IP = "127.0.0.1";

    public static String getLocalIpByFirstNetCard() throws SocketException, UnknownHostException {
        for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();enumeration.hasMoreElements();){
            NetworkInterface item = enumeration.nextElement();
            for (InterfaceAddress address : item.getInterfaceAddresses()){
                if (!(item.isLoopback() || !item.isUp()) && address.getAddress() instanceof Inet4Address){
                    Inet4Address inet4Address = (Inet4Address) address.getAddress();
                    return inet4Address.getHostAddress();
                }
            }
        }
        return getLocalhostIp();
    }

    public static String getLocalhostIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    //根据请求获取IP地址
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null) {
            //对于通过多个代理的情况，最后IP为客户端真实IP,多个IP按照','分割
            int position = ip.indexOf(",");
            if (position > 0) {
                ip = ip.substring(0, position);
            }
        }
        return ip;
    }
}
