package com.example.business_server.entity;

import lombok.Data;

import java.util.List;

@Data
public class Staff {
    private int staff_id;
    private String staff_name;
    private String staff_idcard;
    private String staff_sex;
    private String staff_native;
    private String staff_nation;
    private String staff_position;
    private List<StaffOrder> staffOrders;
}
