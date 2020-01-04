package core.Ioc;

import java.util.HashMap;
import java.util.Map;

public class twoFactory {
    private static class HoldClass{
        private static Map<String,Object> two=new HashMap();
    }

    public static Map getMap(){
        return HoldClass.two;
    }
}
