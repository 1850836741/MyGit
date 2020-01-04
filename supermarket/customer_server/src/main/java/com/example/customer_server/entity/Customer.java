package com.example.customer_server.entity;

<<<<<<< HEAD

import lombok.Data;

import java.util.List;

@Data
public class Customer {
    private String customer_telephone_number;
    private String customer_name;
    private String vip_rank;
    private List<Order> orderList;
=======
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
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
