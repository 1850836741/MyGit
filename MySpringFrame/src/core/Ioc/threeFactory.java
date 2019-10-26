package core.Ioc;

import java.util.HashMap;
import java.util.Map;

public class threeFactory {
    private static class HoldClass{
        private static Map<String,Object> three=new HashMap<>();
    }
    public static Map getMap(){
        return HoldClass.three;
    }
}
