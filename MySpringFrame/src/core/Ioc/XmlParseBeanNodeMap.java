package core.Ioc;

import core.Beans.XmlParseBeanNode;

import java.util.HashMap;
import java.util.Map;

/*存储XML文档解析获取  "Bean"Node的Map集合*/
public class XmlParseBeanNodeMap {
    private static class HoldClass{
        private static Map<String, XmlParseBeanNode> NodeMap = new HashMap<>();
    }

    public static Map<String, XmlParseBeanNode> getXmlParseBeanNodeMap(){
        return HoldClass.NodeMap;
    }

}
