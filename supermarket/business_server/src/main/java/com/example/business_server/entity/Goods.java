package com.example.business_server.entity;

import lombok.Data;

@Data
public class Goods {
    private int goods_id;
    private int goods_price;
    private String goods_name;
    private int goods_number;
    private int goods_originalprice;
    private String goods_source;
}
