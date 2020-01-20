package com.example.oauth2_server.tool;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.BitSet;

/**
 * 布隆过滤器，用以去重
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BloomFilter {

    private static BitSet bloomBitSet = new BitSet(Integer.MAX_VALUE);
    private static final int[] hashParameter = new int[]{7, 13, 41, 31, 73, 133, 232};

    /*将传入对象散列放入布隆过滤器*/
    public  void setInBloomSet(Object object){
        int hashcode = object.hashCode();
        int index = 0;                                                //bloomBitSet的下标
        for (int i = 0;i < hashParameter.length; i++){
            index = hash(i,hashcode);
            bloomBitSet.set(index,true);
        }
    }

    /*取余并且右移散列*/
    private int hash(int i, int code){
        return MathTool.abs(code&(bloomBitSet.length()-1) << hashParameter[i]);
    }

    /*该对象是否存在*/
    public boolean isExist(Object object){
        int hashcode = object.hashCode();
        int index = 0;
        boolean exist = true;
        for (int i = 0; i < hashParameter.length; i++){
            if (!bloomBitSet.get(hash(i,hashcode))){
                exist = false;
            }
        }
        return exist;
    }
}
