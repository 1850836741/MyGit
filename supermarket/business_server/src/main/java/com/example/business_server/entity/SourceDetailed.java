package com.example.business_server.entity;

import lombok.Data;

@Data
public class SourceDetailed {
    private String source_order_id;
    private int source_goods_id;
    private String source_goods_name;
    private int source_subtotal;
    private String source_time;
}
