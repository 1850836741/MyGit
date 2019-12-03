package com.example.customer_server.entity.message;

import com.example.customer_server.entity.Customer;
import lombok.Data;

@Data
public class AddCustomerMessage implements Message{
    private String tableName;
    private Customer customer;

    public AddCustomerMessage(){}

    public AddCustomerMessage(String tableName,Customer customer){
        this.tableName = tableName;
        this.customer = customer;
    }
}
