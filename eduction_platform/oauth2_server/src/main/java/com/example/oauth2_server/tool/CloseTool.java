package com.example.oauth2_server.tool;

import java.io.Closeable;
import java.io.IOException;

public class CloseTool {                                       //关闭单个流
    public static void Close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * length为总数-1
     * @param length
     * @param closeables
     */
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
