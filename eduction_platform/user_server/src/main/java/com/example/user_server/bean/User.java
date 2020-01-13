package com.example.user_server.bean;

import lombok.Data;

import java.util.BitSet;

/**
 * 用户实体类
 */
@Data
public class User {
    private int user_id;
    private String password;
    private String name;
    private BitSet bitSet;
    private int cell_phone;
    private short grade;
    private short jurisdiction;

    public User(){
    }

    public User(int user_id,String password,int cell_phone){
        this.user_id = user_id;
        this.password = password;
        this.cell_phone = cell_phone;
    }
}
