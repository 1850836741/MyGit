package core.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装XML文件中一个bean节点中的信息
 */
public class XmlParseBeanNode {
    private String className;
    private String id;
    private String scope;
    private List<AttributeParameter> constructorList;
    private List<AttributeParameter> propertyList;

    /*建造者模式*/
    public static class Builder{
        private String className;
        private String id;
        private String scope;
        private List<AttributeParameter> constructorList;
        private List<AttributeParameter> propertyList;

        public Builder(String className,String scope){
            this.className=className;
            this.scope=scope;
            id = className.substring(className.lastIndexOf(".")+1);
            id = id.substring(0,1).toLowerCase().concat(id.substring(1));
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder addConstructor(AttributeParameter attributeParameter){
            if (constructorList == null){
                constructorList = new ArrayList<>();
            }
            constructorList.add(attributeParameter);
            return this;
        }

        public Builder addProperty(AttributeParameter attributeParameter){
            if (propertyList == null){
                propertyList = new ArrayList<>();
            }
            propertyList.add(attributeParameter);
            return this;
        }
        public XmlParseBeanNode build(){
            return new XmlParseBeanNode(this);
        }
    }

    public XmlParseBeanNode(Builder builder){
        this.className=builder.className;
        this.scope=builder.scope;
        this.id=builder.id;
        this.constructorList=builder.constructorList;
        this.propertyList=builder.propertyList;
    }

    public void addConstructor(AttributeParameter attributeParameter){
        if (constructorList == null){
            constructorList = new ArrayList<>();
        }
        constructorList.add(attributeParameter);
    }

    public void addProperty(AttributeParameter attributeParameter){
        if (propertyList == null){
            propertyList = new ArrayList<>();
        }
        propertyList.add(attributeParameter);
    }

    public List<AttributeParameter> getConstructorList() {
        return constructorList;
    }

    public List<AttributeParameter> getPropertyList() {
        return propertyList;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClassName() {
        return className;
    }

    public String getId() {
        return id;
    }

    public String getScope() {
        return scope;
    }

}
