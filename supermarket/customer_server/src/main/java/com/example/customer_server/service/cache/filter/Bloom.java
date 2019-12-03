package com.example.customer_server.service.cache.filter;

import java.net.URL;
import java.net.URLConnection;
import java.util.BitSet;


/**
 * 仿造布隆过滤器
 */
public class Bloom {

    private BitSet bitSet=new BitSet(Integer.MAX_VALUE);

    /**
     * 判断是set中否有该key
     * @param key
     * @return
     */
    public synchronized boolean isExist(String key) {
        boolean isExist=true;
        int[] index = getIndex(key);

        for(int value:index) {
            if(bitSet.get(value)) {
                continue;
            }else {
                isExist=false;
            }
        }
        return isExist;
    }

    /**
     * 向布隆过滤器中添加key
     * @param key
     */
    public synchronized void add(String key){
        if (isExist(key)){
            return;
        }else {
            int[] index = getIndex(key);
            for (int value:index){
                bitSet.set(value,true);
            }
        }
    }

    public int[] getIndex(String key){
        int[] index = new int[7];
        int code=Math.abs(key.hashCode());
        index[0]=getIndex7(code);
        index[1]=getIndex79(code);
        index[2]=getIndex139(code);
        index[3]=getIndex503(code);
        index[4]=getIndex773(code);
        index[5]=getIndex1163(code);
        index[6]=getIndex2063(code);
        return index;
    }

    public  int getIndex7(int i) {
        return Math.abs(((i*2%Integer.MAX_VALUE)%7));
    }

    public  int getIndex79(int i) {
        return Math.abs(((i*10%Integer.MAX_VALUE)%79));
    }

    public  int getIndex139(int i) {
        return Math.abs(((i*20%Integer.MAX_VALUE)%139));
    }

    public  int getIndex503(int i) {
        return Math.abs(((i*100%Integer.MAX_VALUE)%503));
    }

    public  int getIndex773(int i) {
        return Math.abs(((i*120%Integer.MAX_VALUE)%773));
    }

    public  int getIndex1163(int i) {
        return Math.abs(((i*260%Integer.MAX_VALUE)%1163));
    }

    public  int getIndex2063(int i) {
        return Math.abs(((i*350%Integer.MAX_VALUE)%2063));
    }

    public  int rehash(int i) {
        return (Math.abs(i*(i>>>16)));
    }
}