package com.example.customer_server.Unit;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭IO流的工具类
 */
public class CloseUnit {                                       //关闭单个流
    public static void Close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Close(int length,Closeable...closeables){               //递归关闭多个流,length是流的个数e
        if (closeables!=null){
            if(length>=0){
                try {
                    Close(length-1,closeables);
                    closeables[length].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                return;
            }
        }
    }
}

