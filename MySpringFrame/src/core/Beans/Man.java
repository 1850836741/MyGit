package core.Beans;

import core.Beans.Annotation.Component;


@Component
public class Man {
    String Name;
    User user;
    public Man(){
    }
    public Man(String Name){
        this.Name=Name;
    }
    public Man(String Name,User user){
        this.Name=Name;
        this.user=user;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
