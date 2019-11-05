package core.Aop;

import core.Aop.Intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AfterIntercept implements Intercept{
    Intercept nextIntercept;
    Method method;

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setNextIntercept(Intercept nextIntercept) {
        this.nextIntercept = nextIntercept;
    }

    public Intercept getNextIntercept() {
        return nextIntercept;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public void doIntercept() {
        if (nextIntercept!=null){
            try {
                nextIntercept.doIntercept();
            }finally {
                try {
                    this.invoke(method);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void invoke(Method method) throws InvocationTargetException, IllegalAccessException {
        method.invoke(null,null);
    }
}
