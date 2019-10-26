package core.Beans;

/**
 * 测试
 */
public class User {
    String Name;
    public User(){

    }
    public User(String Name){
        this.Name=Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }
}
