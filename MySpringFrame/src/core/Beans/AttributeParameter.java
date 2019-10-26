package core.Beans;

/**
 * 用于封装对象参数的类
 * @param <T>
 */
public class AttributeParameter<T> {
    private String parameterName;
    private String parameterType;
    private String refId;
    private T parameterExample;

    public AttributeParameter(){this.parameterType = "java.lang.String";}

    public AttributeParameter(T parameterExample){
        this.parameterExample = parameterExample;
        this.parameterType = "java.lang.String";
    }

    public AttributeParameter(String parameterName , T parameterExample){
        this.parameterName = parameterName;
        this.parameterExample = parameterExample;
        this.parameterType = "java.lang.String";
    }

    public AttributeParameter(String parameterName ,String parameterType ,T parameterExample){
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.parameterExample = parameterExample;
    }

    public String getParameterName() {
        return parameterName;
    }

    public T getParameterExample() {
        return parameterExample;
    }

    public void setParameterExample(T parameterExample) {
        this.parameterExample = parameterExample;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }
}
