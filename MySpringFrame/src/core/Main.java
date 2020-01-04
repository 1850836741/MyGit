package core;

import core.Aop.ProxyFactory;
import core.Beans.Person;
import core.Beans.User;
import core.Ioc.ClassBeanFactory;
import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        ClassBeanFactory classBeanFactory = new ClassBeanFactory(User.class);
        Person user = (Person) classBeanFactory.getBean("user");
        user.getName();
        Map<String,Object> map = classBeanFactory.getMap();
        for (String s:map.keySet()){
            System.out.println(s);
        }

        for (String id : ProxyFactory.getFactory().keySet()){
            System.out.println(id+"=====");
        }
    }
}
