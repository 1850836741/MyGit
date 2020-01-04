package com.example.business_server.entity;

import lombok.Data;

@Data
public class OrderInformation {
    private String order_id;
    private int order_goods_id;
    private int order_goods_total_sum;
    private int order_goods_unitprice;
    private int order_goods_number;
}
