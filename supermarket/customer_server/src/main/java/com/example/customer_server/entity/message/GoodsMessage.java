package com.example.customer_server.entity.message;

import lombok.Data;

@Data
public class GoodsMessage<T> implements Message{

    private String tableName;

    //行为
    private String behavior;

    //标识
    private int objectId;

    private T Object;

    public GoodsMessage(){}

    public GoodsMessage(String tableName, String behavior, int objectId, T Object){
        this.tableName = tableName;
        this.behavior = behavior;
        this.objectId = objectId;
        this.Object = Object;
    }
}
