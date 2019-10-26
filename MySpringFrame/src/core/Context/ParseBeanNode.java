package core.Context;


import core.Beans.AttributeParameter;
import core.Beans.XmlParseBeanNode;
import core.Ioc.XmlParseBeanNodeMap;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 解析XML配置文件的Bean
 */
public class ParseBeanNode {

    /*获取bean节点的nodeList*/
    public static void parse(Document document){
        NodeList nodeList=document.getElementsByTagName("bean");
        parseBeanAttribute(nodeList);
    }

    /*解析nodeList中的每个bean及其属性*/
    public static void parseBeanAttribute(NodeList nodeList){
        String className= "";
        String id = "";
        String scope = "";

        XmlParseBeanNode xmlParseBeanNode;
        Node node;
        NamedNodeMap namedNodeMap;

        for (int i=0;i<nodeList.getLength();i++){
            node=nodeList.item(i);
            //如果是真实的bean节点
            if (node.getNodeType()!=Node.TEXT_NODE){
                namedNodeMap=node.getAttributes();

                for (int j=0;j<namedNodeMap.getLength();j++){
                    //将bean节点的属性,例如class,id等属性放入Map集合并传入下一级解析
                    if (namedNodeMap.item(j).getNodeName().equals("class")){
                        className = namedNodeMap.item(j).getNodeValue();
                    }else if (namedNodeMap.item(j).getNodeName().equals("scope")){
                        scope = namedNodeMap.item(j).getNodeValue();
                    }else if (namedNodeMap.item(j).getNodeName().equals("id")){
                        id = namedNodeMap.item(j).getNodeValue();
                    }
                }
                if (id.equals("")){
                    id = className.substring(className.lastIndexOf(".")+1);
                    id = id.substring(0,1).toLowerCase().concat(id.substring(1));
                    xmlParseBeanNode = new XmlParseBeanNode.Builder(className,scope).setId(id).build();
                }else {
                    xmlParseBeanNode = new XmlParseBeanNode.Builder(className,scope).setId(id).build();
                }
                NodeList parameter= node.getChildNodes();
                //解析bean节点的下级节点信息
                ParseBeanChildNode(xmlParseBeanNode,parameter);
                XmlParseBeanNodeMap.getXmlParseBeanNodeMap().put(id,xmlParseBeanNode);
            }
        }
    }

    /*解析bean的子节点属性,如constructor-arg*/
    public static void ParseBeanChildNode(XmlParseBeanNode xmlParseBeanNode,NodeList parameterList){
        if (parameterList.getLength() > 0){
            for (int i=0;i<parameterList.getLength();i++){
                if (parameterList.item(i).getNodeType() != Node.TEXT_NODE){
                    //是否有构造参数
                    if (parameterList.item(i).getNodeName() == "constructor-arg"){
                        Element constructorElement = (Element) parameterList.item(i);
                        ParseBeanConstructor(constructorElement,xmlParseBeanNode);
                    }else if (parameterList.item(i).getNodeName() == "property"){
                        Element parameterElement = (Element) parameterList.item(i);
                        ParseBeanParameter(parameterElement,xmlParseBeanNode);
                    }
                }
            }
        }else {
            XmlParseBeanNodeMap.getXmlParseBeanNodeMap().put(xmlParseBeanNode.getId(),xmlParseBeanNode);
        }
    }

    /*解析Constructor节点属性,及其子节点*/
    public static void ParseBeanConstructor(Element ConstructorElement, XmlParseBeanNode xmlParseBeanNode){
        String constructorName = ConstructorElement.getAttribute("name")==""?null:ConstructorElement.getAttribute("name");
        //查看是否有list,或者set参数
        if (ConstructorElement.hasChildNodes()){
            NodeList nodeList=ConstructorElement.getChildNodes();
            String constructorParameterType = "list";
            List<String> list = new ArrayList<>();
            for (int i=0;i<nodeList.getLength();i++){
                if (nodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                    //获取list或set标签的子节点队列
                    NodeList lsNodeList = nodeList.item(i).getChildNodes();
                    //遍历list下的value
                    for (int j = 0;j<lsNodeList.getLength();j++){
                        if (lsNodeList.item(j).getNodeType()!=Node.TEXT_NODE){
                            list.add(lsNodeList.item(j).getTextContent());
                        }
                    }
                }
            }
            if (constructorParameterType.equals("list")){
                AttributeParameter<List> attributeParameter= new AttributeParameter<List>(constructorName,list);
                xmlParseBeanNode.addConstructor(attributeParameter);
            }else if (constructorParameterType.equals("set")){
                AttributeParameter<Set> attributeParameter= new AttributeParameter<Set>(constructorName,new HashSet<>(list));
                xmlParseBeanNode.addConstructor(attributeParameter);
            }
        }else {
            if (ConstructorElement.getAttribute("ref") != ""){
                String refId = ConstructorElement.getAttribute("ref");
                AttributeParameter attributeParameter = new AttributeParameter();
                attributeParameter.setParameterName(constructorName);
                attributeParameter.setRefId(refId);
                xmlParseBeanNode.addConstructor(attributeParameter);
            }else {
                String value = ConstructorElement.getAttribute("value")==""?null:ConstructorElement.getAttribute("value");
                xmlParseBeanNode.addConstructor(new AttributeParameter(constructorName,value));
            }
        }
    }

    public static void ParseBeanParameter(Element ParameterElement, XmlParseBeanNode xmlParseBeanNode){
        String parameterName = ParameterElement.getAttribute("name")==""?null:ParameterElement.getAttribute("name");
        //查看是否有list,或者set参数
        if (ParameterElement.hasChildNodes()){
            NodeList nodeList=ParameterElement.getChildNodes();
            String parameterType = "list";
            List<String> list = new ArrayList<>();
            for (int i=0;i<nodeList.getLength();i++){
                if (nodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                    //获取list或set标签的子节点队列
                    parameterType = nodeList.item(i).getNodeName();
                    NodeList lsNodeList = nodeList.item(i).getChildNodes();
                    //遍历list下的value
                    for (int j = 0;j<lsNodeList.getLength();j++){
                        if (lsNodeList.item(j).getNodeType()!=Node.TEXT_NODE){
                            list.add(lsNodeList.item(j).getNodeValue());
                        }
                    }
                }
            }
            if (parameterType.equals("list")){
                AttributeParameter<List> attributeParameter= new AttributeParameter<List>(parameterName,list);
                xmlParseBeanNode.addProperty(attributeParameter);
            }else if (parameterType.equals("set")){
                AttributeParameter<Set> attributeParameter= new AttributeParameter<Set>(parameterName,new HashSet<>(list));
                xmlParseBeanNode.addProperty(attributeParameter);
            }
        }else {
            if (ParameterElement.getAttribute("ref") != ""){
                String refId = ParameterElement.getAttribute("ref");
                AttributeParameter attributeParameter = new AttributeParameter();
                attributeParameter.setParameterName(parameterName);
                attributeParameter.setRefId(refId);
                xmlParseBeanNode.addProperty(attributeParameter);
            }else {
                String value = ParameterElement.getAttribute("value")==""?null:ParameterElement.getAttribute("value");
                xmlParseBeanNode.addProperty(new AttributeParameter(parameterName,value));
            }
        }
    }
}
