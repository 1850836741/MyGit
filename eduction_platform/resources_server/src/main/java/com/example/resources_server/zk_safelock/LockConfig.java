package com.example.resources_server.zk_safelock;

/**
 * zookeeper锁的定义路径
 */
public class LockConfig {
    public final static String WRITE = "write";
    public final static String READ = "read";
    public final static String MONOPOLY = "monopoly";
    public final static String CUSTOMER_LOCK_PATH = "/supermarket/customerLock/";
    public final static String ORDER_LOCK_PATH = "/supermarket/orderLock/";
    public final static String GOODS_LOCK_PATH = "/supermarket/goodsLock/";
    public final static String SOURCE_LOCK_PATH = "/supermarket/sourceLock/";
    public final static String STAFF_LOCK_PATH = "/supermarket/staffLock/";
}