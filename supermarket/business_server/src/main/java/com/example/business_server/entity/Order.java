package com.example.business_server.entity;

import lombok.Data;

@Data
public class Order {
    private String order_id;
    private int order_purchaser_account;
    private int order_total_sum;
    private String order_time;
}
