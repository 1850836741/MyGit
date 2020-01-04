package com.example.customer_server.entity;

import lombok.Data;

@Data
public class Orders {
    private String order_id;
    private String order_purchaser_account;
    private int order_total_sum;
    private String order_time;
}
