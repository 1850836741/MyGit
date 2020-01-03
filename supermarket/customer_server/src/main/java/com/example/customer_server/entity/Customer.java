package com.example.customer_server.entity;


import lombok.Data;

import java.util.List;

@Data
public class Customer {
    private String customer_telephone_number;
    private String customer_name;
    private String vip_rank;
    private List<Order> orderList;
}
