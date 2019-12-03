package com.example.business_server.entity;

import lombok.Data;

@Data
public class StaffSalaryOrder {
    private String staff_order_id;
    private int staff_salary;
    private int staff_expected_salary;
    private int staff_real_salary;
    private String staff_time;
}
