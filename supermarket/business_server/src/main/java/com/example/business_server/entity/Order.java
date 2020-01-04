package com.example.business_server.entity;

import lombok.Data;

<<<<<<< HEAD
import java.util.List;

@Data
public class Order {
    private String order_id;
    private int order_total_price;
    private String order_purchaser;
    private String order_time;
    private List<OrderDetailed> orderDetaileds;
=======
@Data
public class Order {
    private String order_id;
    private int order_purchaser_account;
    private int order_total_sum;
    private String order_time;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
