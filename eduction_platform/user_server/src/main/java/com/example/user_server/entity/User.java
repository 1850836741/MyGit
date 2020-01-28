package com.example.user_server.entity;

import lombok.Data;

import java.util.BitSet;
import java.util.Objects;

/**
 * 用户实体类
 */
@Data
public class User {
    private int user_id;
    private String password;
    private String name;
    private BitSet sex;
    private String cell_phone;
    private short grade;
    private short jurisdiction;
    private String creat_time;

    public User(){
    }

    public User(int user_id,String password,String cell_phone){
        this.user_id = user_id;
        this.password = password;
        this.cell_phone = cell_phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id == user.user_id &&
                cell_phone == user.cell_phone &&
                grade == user.grade &&
                jurisdiction == user.jurisdiction &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(sex, user.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, password, name, sex, cell_phone, grade, jurisdiction);
    }
}
