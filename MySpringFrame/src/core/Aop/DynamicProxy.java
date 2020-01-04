package core.Aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class DynamicProxy implements InvocationHandler {
    Map<String, Intercept> methodMap;
    Object tag;

    public Object bind(Object tag,Map<String,Intercept> methodMap){
        this.tag=tag;
        this.methodMap=methodMap;
        return Proxy.newProxyInstance(tag.getClass().getClassLoader(),tag.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if (methodMap.containsKey(method.getName())){
            methodMap.get(method.getName()).doIntercept();
        }else {
            return method.invoke(tag,objects);
        }
        return null;
    }
}
