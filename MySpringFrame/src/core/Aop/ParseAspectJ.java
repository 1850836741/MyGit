package core.Aop;


import core.Aop.Annotation.After;
import core.Aop.Annotation.Before;
import core.Ioc.ClassBeanFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ParseAspectJ {

    public static void Parse(ClassBeanFactory classBeanFactory) throws ClassNotFoundException {
        Class beanClass = null;
        Map<String,Class> proxyFactory = ProxyFactory.getFactory();
        for (String id:proxyFactory.keySet()){
            HeadIntercept headIntercept = new HeadIntercept();
            Intercept indexIntercept = headIntercept.nextIntercept;
            Intercept lastIntercept = headIntercept;
            Method[] methods = proxyFactory.get(id).getDeclaredMethods();
            String name=null;
            for (int i=0;i<methods.length;i++){
                if (methods[i].getAnnotation(Before.class)!=null){
                    Before before = methods[i].getAnnotation(Before.class);
                    String methodName = before.methodName();
                    beanClass = ParseAspectJ.class.getClassLoader().loadClass(methodName.substring(0,methodName.lastIndexOf(".")));
                    String realMethodName = methodName.substring(methodName.lastIndexOf(".")+1,methodName.length());
                    name = realMethodName;
                    BeforeIntercept beforeIntercept = new BeforeIntercept();
                    beforeIntercept.setMethod(methods[i]);
                    headIntercept.setNextIntercept(beforeIntercept);
                    lastIntercept=lastIntercept.getNextIntercept();
                    indexIntercept=headIntercept.getNextIntercept();
                }else if (methods[i].getAnnotation(After.class)!=null){
                    After after = methods[i].getAnnotation(After.class);
                    String methodName = after.methodName();
                    beanClass = ParseAspectJ.class.getClassLoader().loadClass(methodName.substring(0,methodName.lastIndexOf(".")));
                    String realMethodName = methodName.substring(methodName.lastIndexOf(".")+1,methodName.length());
                    name = realMethodName;
                    AfterIntercept afterIntercept = new AfterIntercept();
                    afterIntercept.setMethod(methods[i]);
                    lastIntercept.setNextIntercept(afterIntercept);
                    lastIntercept = lastIntercept.getNextIntercept();
                }
            }
            Map<String,Intercept> interceptMap = new HashMap<>();
            interceptMap.put(name,headIntercept);
            Map<String,Object> map = classBeanFactory.getMap();
            if (beanClass!=null){
                for (String beanID : map.keySet()){
                    if (map.get(beanID).getClass().equals(beanClass)){
                        Object bean = new DynamicProxy().bind(map.get(beanID),interceptMap);
                        map.put(beanID,bean);
                    }
                }
            }

        }
    }
}
