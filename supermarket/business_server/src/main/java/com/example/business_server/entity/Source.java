package com.example.business_server.entity;

import lombok.Data;

import java.util.List;

@Data
public class Source {
    private String source_order_id;
    private int source_total_price;
    private String source_of_supply;
    private String source_time;
    List<SourceDetailed> sourceDetaileds;
}
