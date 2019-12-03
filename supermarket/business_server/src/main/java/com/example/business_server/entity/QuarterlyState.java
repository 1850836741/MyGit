package com.example.business_server.entity;

import lombok.Data;

@Data
public class QuarterlyState {
    private int quarterly_id;
    private int quarterly_pay;
    private int quarterly_income;
    private int quarterly_total_salary;
    private int quarterly_profit;
    private String quarterly_time;
}
