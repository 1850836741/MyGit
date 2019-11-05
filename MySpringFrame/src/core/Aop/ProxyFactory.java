package core.Aop;

import java.util.HashMap;
import java.util.Map;

/**
 * 代理池,用于存放设置了切面的Bean
 */
public class ProxyFactory {
    private static class HoldClass{
        private static Map<String,Class> map = new HashMap<>();
    }

    public static Map<String,Class> getFactory(){
        return HoldClass.map;
    }
}
