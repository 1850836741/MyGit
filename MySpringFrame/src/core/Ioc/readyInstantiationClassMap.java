package core.Ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 存储注解配置需要实例化的class的Map
 */
public class readyInstantiationClassMap {

    private static class HoldClass{
        private static Map<String,Class> map = new HashMap<>();
    }

    public static Map<String, Class> getMap(){
        return HoldClass.map;
    }
}
