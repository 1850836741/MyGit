package core.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装构造方法形参名字
 */
public class MethodBean {
    List<String> nameList;

    public MethodBean(){
        nameList = new ArrayList<>();
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void add(String name){
        nameList.add(name);
    }
}
