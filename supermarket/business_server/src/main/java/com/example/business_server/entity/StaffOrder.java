package com.example.business_server.entity;

import lombok.Data;

@Data
public class StaffOrder {
    private String staff_order_id;
    private int staff_id;
    private int staff_salary;
    private String staff_salary_time;
}
