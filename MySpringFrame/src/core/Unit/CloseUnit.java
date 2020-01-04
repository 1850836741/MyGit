package core.Unit;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭IO流的工具
 */
public class CloseUnit {

    /*关闭单个流*/
    public static void close(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*关闭多个流*/
    public static void close(int length,Closeable...closeables){
        if (closeables!=null){
            if (length>0){
                close(length-1,closeables);
                try {
                    closeables[length-1].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
