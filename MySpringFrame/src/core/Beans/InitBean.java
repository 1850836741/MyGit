package core.Beans;

import core.Ioc.XmlParseBeanNodeMap;
import core.Ioc.oneFactory;
import core.Ioc.threeFactory;
import core.Ioc.twoFactory;
import core.Unit.ClassUnit;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对Bean进行初始化
 */
public class InitBean {


    public static void instantiation(String beanId){
        XmlParseBeanNode xmlParseBeanNode = XmlParseBeanNodeMap.getXmlParseBeanNodeMap().get(beanId);
        ClassLoader classLoader = InitBean.class.getClassLoader();
        //无构造参数
        if (xmlParseBeanNode.getConstructorList()==null&&xmlParseBeanNode.getPropertyList()==null){
            try {
                Class beanClass = classLoader.loadClass(xmlParseBeanNode.getClassName());
                Object beanObject = beanClass.newInstance();
                oneFactory.getMap().put(beanId,beanObject);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            //含有构造参数
        }else {
            try {
                Class beanClass = classLoader.loadClass(xmlParseBeanNode.getClassName());
                //获取Xml中配置的构造方法的参数
                List<AttributeParameter> constructorList = xmlParseBeanNode.getConstructorList();
                //获取class对象的所有构造方法及其参数名字
                List<MethodBean> methodBeans = GetMethodBeanList(xmlParseBeanNode.getClassName());
                Constructor[] constructors=beanClass.getDeclaredConstructors();
                boolean isMatch = true;

                //存储无名字参数对象
                List<AttributeParameter> list = new ArrayList<>();
                //存储有名字参数对象
                Map<String,AttributeParameter> map = new HashMap<>();
                AttributeParameter attributeParameter=null;
                for (int i = 0 ;i < xmlParseBeanNode.getConstructorList().size();i++){
                    attributeParameter = xmlParseBeanNode.getConstructorList().get(i);
                    if (xmlParseBeanNode.getConstructorList().get(i).getParameterName() != null){
                        //如果有依赖，则注入依赖，否则注入配置的值
                        if (attributeParameter.getRefId() != null){
                            attributeParameter.setParameterExample(getRef(attributeParameter.getRefId()));
                        }
                        map.put(attributeParameter.getParameterName(), attributeParameter);
                    }else {
                        list.add(attributeParameter);
                    }
                }

                if (map.size()>0){
                    for (int i =0 ;i<methodBeans.size();i++){
                        //找到与配置构造参数长度相等的构造方法
                        if (methodBeans.get(i).getNameList().size() == xmlParseBeanNode.getConstructorList().size()){
                            List<String> nameList = methodBeans.get(i).getNameList();
                            for (String name:map.keySet()){
                                if (!nameList.contains(name)){
                                    isMatch = false;
                                }
                            }
                            //确定是该构造方法
                            if (isMatch){
                                //装有无名参数list的遍历下标指向
                                int index = 0;
                                List<AttributeParameter> parameterList = new ArrayList<>();
                                for (int j = 0;j < nameList.size();j++){
                                    if (map.get(nameList.get(j))!=null){
                                        parameterList.add(map.get(nameList.get(j)));
                                    }else {
                                        parameterList.add(list.get(index));
                                        index++;
                                    }
                                }
                                doInit(xmlParseBeanNode,constructors[i],parameterList);
                                if (xmlParseBeanNode.getPropertyList()!=null){
                                    //属性注入
                                    DI(xmlParseBeanNode);
                                }
                                break;
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < methodBeans.size(); i++){
                        //找到与配置构造参数长度相等的构造方法
                        if (constructors[i].getParameterTypes().length == list.size()){
                            Class[] classes = constructors[i].getParameterTypes();
                            //为参数AttributeParameter对象设置对象类型
                            for (int j = 0;j < classes.length;j ++){
                                list.get(j).setParameterType(classes[j].getName());
                            }
                            //实例化
                            doInit(xmlParseBeanNode,constructors[i],list);
                            if (xmlParseBeanNode.getPropertyList()!=null){
                                //属性注入
                                DI(xmlParseBeanNode);
                            }
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /*获取class对象的所有构造方法及其参数名字*/
    public static List<MethodBean> GetMethodBeanList(String classname){
        String classURL = classname.replace(".","/").concat(".class");
        try {
            return ClassUnit.GetMethodParameters(classURL);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    /*处理依赖*/
    public static Object getRef(String refId){
        Object refBean = oneFactory.getMap().get(refId) == null? twoFactory.getMap().get(refId):oneFactory.getMap().get(refId);
        if (refBean == null){
            refBean = threeFactory.getMap().get(refId);
        }
        if (refBean == null){
            instantiation(refId);
            refBean = getRef(refId);
        }
        return refBean;
    }


    /*真正进行初始化的方法*/
    public static void doInit(XmlParseBeanNode xmlParseBeanNode,Constructor constructor,List<AttributeParameter> list){
        Class[] classes = constructor.getParameterTypes();
        //将构造参数的类型统一，方便接下来类型转换
        for (int i = 0;i < classes.length;i++){
            list.get(i).setParameterType(classes[i].getCanonicalName());
        }
        List<Object> parameterList = new ArrayList<>();
        for (int i = 0;i<classes.length;i++){
            parameterList.add(classes[i].equals(getRealType(list.get(i)).getClass())?getRealType(list.get(i)):null);
        }
        try {
            if (xmlParseBeanNode.getPropertyList()!=null){
                //放入二级缓存
                twoFactory.getMap().put(xmlParseBeanNode.getId(),constructor.newInstance(parameterList.toArray()));
            }else {
                oneFactory.getMap().put(xmlParseBeanNode.getId(),constructor.newInstance(parameterList.toArray()));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /*依赖注入*/
    public static void DI(XmlParseBeanNode xmlParseBeanNode){
        Object objectBean = twoFactory.getMap().get(xmlParseBeanNode.getId());
        Class beanClass = objectBean.getClass();
        Method[] methods=beanClass.getMethods();
        Map<String,Method> map=new HashMap<>();
        for (int i=0;i<methods.length;i++){
            map.put(methods[i].getName(),methods[i]);
        }
        List<AttributeParameter> list = xmlParseBeanNode.getPropertyList();
        String upperName = null;
        String methodName = null;
        for (int i = 0;i < list.size();i++){
            upperName=list.get(i).getParameterName();
            upperName=upperName.substring(0,1).toUpperCase().concat(list.get(i).getParameterName().substring(1));
            methodName="set".concat(upperName);
            if (list.get(i).getRefId()!=null){
                list.get(i).setParameterExample(getRef(list.get(i).getRefId()));
            }
            try {
                map.get(methodName).invoke(objectBean,list.get(i).getParameterExample());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        oneFactory.getMap().put(xmlParseBeanNode.getId(),objectBean);
        twoFactory.getMap().remove(xmlParseBeanNode.getId());
    }


    /*转化数据类型*/
    public static Object getRealType(AttributeParameter attributeParameter){
        if (!attributeParameter.getParameterType().contains("String")) {
            if (attributeParameter.getParameterType().contains("Byte")){
                return Byte.parseByte((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Integer")|attributeParameter.getParameterType().contains("int")){
                return Integer.parseInt((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Long")){
                return Long.parseLong((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Double")){
                return Double.parseDouble((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Data")){
                return new Date((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Float")){
                return Float.parseFloat((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Boolean")){
                return Boolean.parseBoolean((String) attributeParameter.getParameterExample());
            }else if (attributeParameter.getParameterType().contains("Short")) {
                return Short.parseShort((String) attributeParameter.getParameterExample());
            }else {
                return attributeParameter.getParameterExample();
            }
        }
        return attributeParameter.getParameterExample().toString();
    }
}
