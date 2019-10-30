package core;

import core.Beans.User;
import core.Ioc.ClassBeanFactory;
import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        ClassBeanFactory classBeanFactory = new ClassBeanFactory(User.class);
        User user = (User) classBeanFactory.getBean("user");
        Map<String,Object> map = classBeanFactory.getMap();
        for (String s:map.keySet()){
            System.out.println(s);
        }
    }
}
