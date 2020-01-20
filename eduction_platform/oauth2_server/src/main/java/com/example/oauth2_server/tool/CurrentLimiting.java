package com.example.oauth2_server.tool;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 漏斗限流服务，防止服务器压力过大
 */
@Data
public class CurrentLimiting {
    public int capacity;                  //容量
    public float currentSpeed;            //流速
    public volatile AtomicInteger surplusSpace;    //剩余空间
    public long lastTime;                 //上一次灌水时间


    public CurrentLimiting(){
        this.capacity = 100000;
        this.currentSpeed = currentSpeed;
        this.surplusSpace = new AtomicInteger(capacity);
        this.lastTime = System.currentTimeMillis();
    }

    public CurrentLimiting(int capacity){
        this.capacity = capacity;
        this.currentSpeed = 0.05f;
        this.surplusSpace = new AtomicInteger(capacity);
        this.lastTime = System.nanoTime();
    }

    public CurrentLimiting(int capacity, float currentSpeed){
        this.capacity = capacity;
        this.currentSpeed = currentSpeed;
        this.surplusSpace = new AtomicInteger(capacity);
        this.lastTime = System.nanoTime();
    }

    /**
     * 获取流量资格
     * @return
     */
    public boolean in(){
        boolean setSuccess = false;                            //是否设置成功
        int frequency = 50;
        long nowTime = System.nanoTime();
        long intervalTime = nowTime - lastTime;                //间隔时间
        lastTime = nowTime;
        do {
            frequency--;
            if (surplusSpace.get() < capacity*0.10){
                setSuccess = surplusSpace.compareAndSet(surplusSpace.get(), (int) (intervalTime*currentSpeed));
                if (surplusSpace.get() >= capacity){
                    surplusSpace.set(0);
                }
            }else {
                setSuccess =surplusSpace.compareAndSet(surplusSpace.get(),intervalTime*currentSpeed > capacity? 0 : (int) (capacity - intervalTime * currentSpeed));
            }
        }while (setSuccess || frequency>0);
        return setSuccess;
    }
}
