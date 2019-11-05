package core.Beans;

import core.Aop.ParseAspectJ;
import core.Beans.Annotation.Autowired;
import core.Ioc.ClassBeanFactory;
import core.Ioc.readyInstantiationClassMap;
import core.Unit.UnsafeUnit;;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 对注解配置的Bean进行初始化
 */
public class ClassInitBean {

    /*简单初始化并把对象装入工厂的Map中*/
    public static void instantiation(ClassBeanFactory classBeanFactory){
        Map<String,Class> map = readyInstantiationClassMap.getMap();
        for (String id: map.keySet()){
            Class beanClass = map.get(id);
            try {
                Object beanObject = beanClass.newInstance();
                classBeanFactory.getMap().put(id,beanObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            //切面处理
            ParseAspectJ.Parse(classBeanFactory);
            //依赖注入
            DI(classBeanFactory,map);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*对装入Map中的对象进行属性注入*/
    public static void DI(ClassBeanFactory classBeanFactory, Map<String,Class> map) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Field[] fields = null;
        Method[] methods = null;
        Map<String,Object> beanMap = classBeanFactory.getMap();
        for (String beanId:map.keySet()){
            Class beanClass = map.get(beanId);
            fields = beanClass.getDeclaredFields();
            //遍历字段
            for (Field field : fields){
                if (field.getAnnotation(Autowired.class)!=null){
                    for (Object object : beanMap.values()){
                        if (object.getClass().equals(field.getClass())){
                            //为对象赋值
                            UnsafeUnit.setObject(beanMap.get(beanId),field.getName(),null, object);
                        }
                    }
                }
            }

            methods = beanClass.getDeclaredMethods();
            //遍历方法
            for (Method method : methods){
                if (method.getAnnotation(Autowired.class)!=null){
                    Class[] parameterTypes = method.getParameterTypes();
                    Object[] beanParameterType = new Object[parameterTypes.length];
                    for (int i = 0;i<parameterTypes.length; i++){
                        for (Object object : beanMap.values()){
                            if (parameterTypes[i].equals(object)){
                                beanParameterType[i] = object;
                            }
                        }
                    }
                    method.invoke(beanMap.get(beanId),beanParameterType);
                }
            }
        }
    }
}
