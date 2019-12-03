package com.example.customer_server.entity.message;

import lombok.Data;

@Data
public class OrderMessage<T> implements Message{

    private String tableName;

    //行为
    private String behavior;

    //标识
    private String objectId;

    private T Object;

    public OrderMessage(){}

    public OrderMessage(String tableName, String behavior, String objectId, T Object){
        this.tableName = tableName;
        this.behavior = behavior;
        this.objectId = objectId;
        this.Object = Object;
    }
}
