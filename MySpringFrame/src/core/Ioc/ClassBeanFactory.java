package core.Ioc;

import core.Beans.ClassInitBean;
import core.Context.ParseBeanClass;

import java.util.HashMap;
import java.util.Map;

/**
 *JavaConfig上下文
 */
public class ClassBeanFactory implements BeanFactory{
    Map<String,Object> map;
    Class baseClass;

    public ClassBeanFactory(Class baseClass){
        this.baseClass = baseClass;
        map = new HashMap<>();
    }

    @Override
    public Object getBean(String name) {
        if (map.get(name)==null){
            try {
                ParseBeanClass.Parse(baseClass);
                ClassInitBean.instantiation(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.get(name);
    }

    @Override
    public Object getBean(Class beanClass) {
        return null;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
