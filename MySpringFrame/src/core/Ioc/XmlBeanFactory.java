package core.Ioc;

import core.Beans.InitBean;
import core.Context.OpenXmlResources;
import core.Context.ParseBeanNode;
import java.util.Map;

/**
 * Xml上下文
 * @param <T>
 */
public class XmlBeanFactory<T> implements BeanFactory<T>{

    public XmlBeanFactory(){
        init();
    }

    public void init(){
        ParseBeanNode.parse(OpenXmlResources.getDocument());
    }


    @Override
    public T getBean(String name) {
        T bean = (T) oneFactory.getMap().get(name);
        if (bean==null){
            InitBean.instantiation(name);
        }
        return (T) oneFactory.getMap().get(name);
    }

    @Override
    public T getBean(Class beanClass) {
        Map<String,Object> map = oneFactory.getMap();
        for (String id : map.keySet()){
            if (map.get(id).getClass().equals(beanClass)){
                return (T) map.get(id);
            }
        }
        return null;
    }
}
