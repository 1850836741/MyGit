package com.example.customer_server.entity;

import lombok.Data;

@Data
public class SourceOrder {
    private String source_order_id;
    private int source_order_total_sum;
    private String source_order_time;
    private String source_order_place;
}
