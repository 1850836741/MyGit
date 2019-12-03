package com.example.business_server.entity;

import lombok.Data;

@Data
public class Customer {
    private String customer_account;
    private String customer_password;
    private String customer_role;
    private String registration_time;
    private String customer_name;
    private int customer_age;
    private String customer_sex;
    private String customer_telephone;
    private String is_vip;
}
