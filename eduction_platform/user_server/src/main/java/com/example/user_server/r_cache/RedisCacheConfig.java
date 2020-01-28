package com.example.user_server.r_cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作User的配置文件
 */
public class RedisCacheConfig {
    private static final Map<Integer,String> GRADE_MAP = new HashMap<>(6);
    public static final String USERS_GRADE_HASH = "userGrade";
    public static final int GRADE_MAX = 6;
    public static final int GRADE_MIN = 1;
    public static final int GRADE_1 = 1;
    public static final int GRADE_2 = 2;
    public static final int GRADE_3 = 3;
    public static final int GRADE_4 = 4;
    public static final int GRADE_5 = 5;
    public static final int GRADE_6 = 6;

    /*存储每个用户登陆天数位图的key值前缀(整个名字为loginDay+用户Id)*/
    public static final String USER_LOGIN_DAYS_PREFIX = "loginDay";

    /*合法用户账号最大值*/
    public static final int RIGHTFUL_USER_ID_MAX = Integer.MAX_VALUE;
    /*合法用户账号最小值*/
    public static final int RIGHTFUL_USER_ID_MIN = 100000;

    /*合法密码最大长度*/
    public static final int RIGHTFUL_USER_PASSWORD_SIZE_MAX = 20;
    /*合法密码最小长度*/
    public static final int RIGHTFUL_USER_PASSWORD_SIZE_MIN = 6;

    public static final String ZUUL_SERVER_PREFIX = "http://localhost:8081/";

    /*客户端存储的accessToken的key*/
    public static final String COOKIE_TOKEN_KEY = "accessToken";

    /*客户端存储的refreshToken的key*/
    public static final String COOKIE_REFRESH_TOKEN_KEY = "refreshToken";
    
    public static final short JURISDICTION_MAX = 6;
    public static final short JURISDICTION_MIN = 0;

    public static Map<Integer,String> getGrade_Map(){
        GRADE_MAP.put(1,"学水");
        GRADE_MAP.put(2,"学沫");
        GRADE_MAP.put(3,"学渣");
        GRADE_MAP.put(4,"学民");
        GRADE_MAP.put(5,"学爸");
        GRADE_MAP.put(6,"学神");
        return GRADE_MAP;
    }
}
