package com.example.customer_server.entity.message;

import com.example.customer_server.entity.Customer;
import lombok.Data;

@Data
public class DeleteCustomerMessage {
    private String tableName;
    private String customer_account;

    public DeleteCustomerMessage(){}

    public DeleteCustomerMessage(String tableName, String customer_account){
        this.tableName = tableName;
        this.customer_account = customer_account;
    }
}
