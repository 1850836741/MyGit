package com.example.user_server.tool;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 数学计算相关的工具类
 */
public class MathTool {

    /**
     * 两数交换
     * @param a
     * @param b
     */
    public static void swap(Integer a, Integer b, int[] arr){
        arr[a] ^= arr[b];
        arr[b] ^= arr[a];
        arr[a] ^= arr[b];
    }

    /**
     * 判断是否为偶数
     * @param a
     * @return
     */
    public static boolean judgeEvenNumber(int a){
        return 0==(a & 1);
    }

    /**
     * 取反
     * @param a
     * @return
     */
    public static int reversal(int a){
        return ~a + 1;
    }

    /**
     * 取绝对值
     * @param a
     * @return
     */
    public static int abs(int a){
        int i = a >> 31;
        return ((a^i) - i);
    }

    /**
     * 获取一定范围内的随机数
     * @param origin
     * @param bound
     * @return
     */
    public static int getRandom(int origin, int bound){
        return ThreadLocalRandom.current().nextInt(origin,bound);
    }

}
