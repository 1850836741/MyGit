package com.example.customer_server.entity;

import lombok.Data;

@Data
public class SourceOrderInformation {
    private String source_order_id;
    private int source_order_goods_id;
    private int source_order_goods_total_sum;
    private int source_order_goods_unitprice;
    private int source_order_goods_number;
}
