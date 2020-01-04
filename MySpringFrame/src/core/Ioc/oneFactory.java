package core.Ioc;

import java.util.HashMap;
import java.util.Map;

public class oneFactory{
    private static class HoldClass{
        private final static Map<String,Object> one=new HashMap<>();
    }
    public static Map getMap(){
        return HoldClass.one;
    }
}
