package com.example.customer_server.entity;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String order_id;
    private int order_total_price;
    private String order_purchaser;
    private String order_time;
    private List<OrderDetailed> orderDetaileds;
}
