package com.example.user_server.tool;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 漏斗限流服务，防止服务器压力过大
 */
@Data
public class CurrentLimiting {
    public int capacity;                  //容量
    public float currentSpeed;            //流速
    public int surplusSpace;              //剩余空间
    public long lastTime;                 //上一次灌水时间

    public CurrentLimiting(){
        this.capacity = 100000;
        this.currentSpeed = currentSpeed;
        this.surplusSpace = capacity;
        this.lastTime = System.currentTimeMillis();
    }

    public CurrentLimiting(int capacity){
        this.capacity = capacity;
        this.currentSpeed = 0.05f;
        this.surplusSpace = capacity;
        this.lastTime = System.nanoTime();
    }

    public CurrentLimiting(int capacity, float currentSpeed){
        this.capacity = capacity;
        this.currentSpeed = currentSpeed;
        this.surplusSpace = capacity;
        this.lastTime = System.nanoTime();
    }

    /**
     * 获取流量资格
     * @return
     */
    public boolean in(){
        long nowTime = System.nanoTime();
        long intervalTime = nowTime - lastTime;
        lastTime = nowTime;
        if (surplusSpace < capacity*0.10){
            surplusSpace += intervalTime*currentSpeed;
            if (surplusSpace > capacity){
                surplusSpace = capacity;
            }
            return false;
        }else {
            surplusSpace = intervalTime*currentSpeed > capacity ? 0 : (int) (capacity - intervalTime * currentSpeed);
        }
        return true;
    }
}
