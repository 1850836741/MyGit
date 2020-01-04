package com.example.customer_server.entity.message;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateCustomerMessage implements Message{
    private String tableName;
    private String customer_account;
    private Map<String,String> updateMap;

    public UpdateCustomerMessage(){
    }

    public UpdateCustomerMessage(String tableName, String customer_account, Map<String,String> updateMap){
        this.tableName = tableName;
        this.customer_account = customer_account;
        this.updateMap = updateMap;
    }
}
