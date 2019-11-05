package core.Aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HeadIntercept implements Intercept{

    Intercept nextIntercept;

    public void setNextIntercept(Intercept nextIntercept) {
        this.nextIntercept = nextIntercept;
    }

    public Intercept getNextIntercept() {
        return nextIntercept;
    }

    @Override
    public void doIntercept() {
        if (nextIntercept!=null){
            nextIntercept.doIntercept();
        }
    }

    @Override
    public void invoke(Method method) throws InvocationTargetException, IllegalAccessException {
    }
}
