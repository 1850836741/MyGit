package core.Beans;

import core.Beans.Annotation.Autowired;
import core.Beans.Annotation.Component;
import core.Beans.Annotation.ComponentScan;

/**
 * 测试
 */
@ComponentScan(basePackageClass = Man.class)
@Component
public class User implements Person{
    String Name;
    String ID;
    @Autowired
    Man man;
    public User(){
    }
    public User(String Name){
        this.Name=Name;
    }

    public User(String Name,String ID){
        this.Name=Name;
        this.ID=ID;
    }

    public User(String Name,String ID,Man man){
        this.Name=Name;
        this.ID=ID;
        this.man=man;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setMan(Man man) {
        this.man = man;
    }

    public Man getMan() {
        return man;
    }
}
