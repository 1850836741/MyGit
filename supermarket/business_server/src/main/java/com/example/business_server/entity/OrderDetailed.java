package com.example.business_server.entity;

import lombok.Data;

@Data
public class OrderDetailed {
    private String order_id;
    private int order_goods_id;
    private String order_goods_name;
    private int order_subtotal;
    private String order_time;
}
