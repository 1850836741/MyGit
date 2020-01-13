package com.example.user_server.tool;

public class MathTool {

    /*两数交换*/
    public static void swap(Integer a, Integer b){
        a ^= b;
        b ^= a;
        a ^= b;
    }

    /*判断是否为偶数*/
    public static boolean judgeEvenNumber(int a){
        return 0==(a & 1);
    }

    /*取反*/
    public static int reversal(int a){
        return ~a + 1;
    }

    /*取绝对值*/
    public static int abs(int a){
        int i = a >> 31;
        return ((a^i) - i);
    }

}
