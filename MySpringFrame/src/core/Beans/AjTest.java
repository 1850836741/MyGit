package core.Beans;

import core.Aop.Annotation.AspectJ;
import core.Aop.Annotation.Before;

@AspectJ
public class AjTest {
    @Before(methodName = "core.Beans.User.getName")
    static public void talk(){
        System.out.println("这是一个切面");
    }
}
