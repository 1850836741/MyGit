package core.Aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeforeIntercept implements Intercept {

    Intercept nextIntercept;
    Method method;

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setNextIntercept(Intercept nextIntercept) {
        this.nextIntercept = nextIntercept;
    }

    public Intercept getNextIntercept() {
        return nextIntercept;
    }

    @Override
    public void doIntercept() {
        try {
            this.invoke(method);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if (nextIntercept!=null){
                nextIntercept.doIntercept();
            }
        }
    }

    @Override
    public void invoke(Method method) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(false);
        method.invoke(null,null);
    }
}
