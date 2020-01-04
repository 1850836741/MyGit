package com.example.business_server.entity;

import lombok.Data;

<<<<<<< HEAD
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
=======
@Data
public class Goods {
    private int goods_id;
    private int goods_price;
    private String goods_name;
    private int goods_number;
    private int goods_originalprice;
    private String goods_source;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
