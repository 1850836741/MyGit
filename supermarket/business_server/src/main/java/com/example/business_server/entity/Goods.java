package com.example.business_server.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Goods implements Serializable {
    private int goods_id;
    private String goods_name;
    private int goods_number;
    private int goods_original_price;
    private int goods_price;
    private String goods_manufacture_data;
    private int goods_quality_time;
}
