package core.Context;

import core.Aop.Annotation.AspectJ;
import core.Aop.ProxyFactory;
import core.Beans.Annotation.Component;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;

import core.Beans.Annotation.ComponentScan;
import core.Ioc.readyInstantiationClassMap;
import java.util.Set;

import core.Ioc.ClassBeanFactory;

/**
 * 通过传入的Class,来找出用户希望初始化的类，并放入Set中
 */
public class ParseBeanClass {

    //防止循环递归Path
    static Set<String> pathSet = new HashSet<>();

    /*获取传入的Class的包信息或者设置的包信息*/
    public static void Parse( Class beanClass) throws Exception {
        if (beanClass.getAnnotation(AspectJ.class)!=null){
            String name = beanClass.getName();
            name = name.substring(beanClass.getName().lastIndexOf(".")+1);
            name = name.substring(0,1).toLowerCase().concat(name.substring(1));
            ProxyFactory.getFactory().put(name,beanClass);
        }
        if (beanClass.getAnnotation(ComponentScan.class)!=null){
            ComponentScan componentScan = (ComponentScan) beanClass.getAnnotation(ComponentScan.class);
            ParseComponentScan(componentScan);
        }
        String beanPackageName = beanClass.getPackageName();
        if (pathSet.contains(beanPackageName)){
            return;
        }
        pathSet.add(beanPackageName);
        Enumeration<URL> urls = null;
        beanPackageName = beanPackageName.replace(".","/");
        try {
            urls = beanClass.getClassLoader().getResources(beanPackageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            try {
                String absolutePath = URLDecoder.decode(url.getPath(),"UTF-8").substring(1);
                //通过绝对路径和包名找出class
                findClass(absolutePath,beanPackageName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    /*找到指定目录下面带有@Component的类，并加入到Set中*/
    public static void findClass(String absolutePath, String beanPackageName) throws Exception {
        File file = new File(absolutePath);
        if (!file.exists()){
            return;
        }
        File[] files = file.listFiles();
        for (File fileInPackage:files){
            if (fileInPackage.isDirectory()){
                findClass(absolutePath+"/"+fileInPackage.getName(),
                        beanPackageName+"."+fileInPackage.getName());
            }
            //如果是Class文件，且带有@Component注解
            if (fileInPackage.getName().contains(".class")&&!fileInPackage.getName().contains("$")){
                String classFullyQualifiedName = beanPackageName.replace("/",".")+"."+fileInPackage.getName().substring(0,fileInPackage.getName().length()-6);
                Class loadClass = ParseBeanClass.class.getClassLoader().loadClass(classFullyQualifiedName);


                //判断该class是否含有AspectJ注解
                if (loadClass.getAnnotation(AspectJ.class)!=null){
                    String classID = classFullyQualifiedName.substring(classFullyQualifiedName.lastIndexOf(".")+1);
                    classID = classID.substring(0,1).toLowerCase().concat(classID.substring(1));
                    ProxyFactory.getFactory().put(classID,loadClass);
                }


                //判断该class是否含有Component注解
                if (loadClass.getAnnotation(Component.class)!=null){
                    Component component = (Component) loadClass.getAnnotation(Component.class);
                    String classID = null;
                    if (!component.id().equals("")){
                        classID = component.id();
                    }else {
                        classID = classFullyQualifiedName.substring(classFullyQualifiedName.lastIndexOf(".")+1);
                        classID = classID.substring(0,1).toLowerCase().concat(classID.substring(1));
                    }
                    readyInstantiationClassMap.getMap().put(classID,loadClass);
                }

                if (loadClass.getAnnotation(ComponentScan.class)!=null){
                    ComponentScan componentScan = (ComponentScan) loadClass.getAnnotation(ComponentScan.class);
                    ParseComponentScan(componentScan);
                }
            }
        }
    }

    /*解析ComponentScan注解所带的信息*/
    public static void ParseComponentScan(ComponentScan componentScan) throws Exception {
        if (!componentScan.basePackage().equals("")){
            String beanPackageName = componentScan.basePackage();
            String absolutePath = System.getProperty("java.class.path").concat("\\");
            absolutePath = absolutePath.replace("\\","/").concat(beanPackageName);
            beanPackageName = beanPackageName.replace(".","/");
            if (pathSet.contains(beanPackageName)){
                return;
            }else {
                pathSet.add(beanPackageName);
                findClass(absolutePath,beanPackageName);
            }
        }else if (!componentScan.basePackages()[0].equals("")){
            for (int i = 0; i < componentScan.basePackages().length ; i++){
                String beanPackageName = componentScan.basePackages()[i];
                String absolutePath = System.getProperty("java.class.path").concat("\\");
                absolutePath = absolutePath.replace("\\","/").concat(beanPackageName);
                beanPackageName = beanPackageName.replace(".","/");
                if (pathSet.contains(beanPackageName)){
                    return;
                }else {
                    pathSet.add(beanPackageName);
                    findClass(absolutePath,beanPackageName);
                }
            }
        }else if (componentScan.basePackageClass()!=Component.class){
            Parse(componentScan.basePackageClass());
        }else if (componentScan.basePackageClasses()[0]!=Component.class){
            for (int i = 0;i < componentScan.basePackageClasses().length;i++){
                Parse(componentScan.basePackageClasses()[i]);
            }
        }
    }
}
