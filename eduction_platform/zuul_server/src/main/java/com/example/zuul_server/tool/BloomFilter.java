package com.example.zuul_server.tool;

import java.util.BitSet;
/**
 * 布隆过滤器，用以去重
 */
public class BloomFilter {

    private static BitSet bloomBitSet = new BitSet(Integer.MAX_VALUE);
    private static final int[] hashParameter = new int[]{7, 13, 41, 31, 73, 133, 232};

    /*将传入对象散列放入布隆过滤器*/
    public static boolean setInBloomSet(Object object){
        int hashcode = object.hashCode();
        int index = 0;
        boolean doNotContain = false;
        for (int i = 0;i < hashParameter.length; i++){
            index = hash(i,hashcode);
            if (!bloomBitSet.get(index)){                        //该位置无值
                doNotContain = true;
            }
            bloomBitSet.set(index,true);
        }
        return doNotContain;                                     //该对象是否曾经存在
    }

    /*取余并且右移散列*/
    private static int hash(int i, int code){
        return MathTool.abs(code&(bloomBitSet.length()-1) << hashParameter[i]);
    }

}
