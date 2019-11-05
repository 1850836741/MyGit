package core.Aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Intercept {
    public void doIntercept();
    public void invoke(Method method) throws InvocationTargetException, IllegalAccessException;
    public void setNextIntercept(Intercept nextIntercept);
    public Intercept getNextIntercept();
}
