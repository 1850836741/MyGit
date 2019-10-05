package SoftwareTest.Test1;

import SoftwareTest.Test1.Unit.*;
import java.io.IOException;
public class ProjectInitiation {

    /**
     * 依据传入unit的类型，来判断是何种服务
     */
    static public void init(Unit unit) throws IOException {
        try {
            unit.Implement();
        }catch (Exception e){
            if(unit instanceof CommodityUnit){
                System.out.println("输入正确的参数...");
            }else if (unit instanceof SmallChangeUnit){
                System.out.println("输入正确的参数...");
            }else if (unit instanceof VipUnit){
                System.out.println("N/A");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProjectInitiation.init(unit);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
