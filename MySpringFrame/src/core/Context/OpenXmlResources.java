package core.Context;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * 获取单例的XML配置文件的Document对象
 */
public class OpenXmlResources {
    /*静态内部类保证单例*/
    private static class HolderClass{
        private final static DocumentBuilderFactory documentBuilderFactory;
        private final static DocumentBuilder documentBuilder;
        private final static Document document;
        static {
            documentBuilderFactory=DocumentBuilderFactory.newInstance();
            documentBuilder=getDocumentBuilder();
            document=getDocument();
        }

        /*try catch不能给静态最终变量赋值*/
        private static DocumentBuilder getDocumentBuilder(){
            try {
                return documentBuilderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                return null;
            }
        }

        private static Document getDocument(){
            try {
                return documentBuilder.parse(new File("src\\Myspring.xml"));
            } catch (SAXException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /*真正获取Document的类*/
    public static Document getDocument(){
        return HolderClass.document;
    }
}
