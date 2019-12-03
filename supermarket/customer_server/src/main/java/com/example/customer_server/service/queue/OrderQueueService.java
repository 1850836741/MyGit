package com.example.customer_server.service.queue;

import com.example.customer_server.entity.Orders;
import com.example.customer_server.entity.message.MessageBehaviorConfig;
import com.example.customer_server.entity.message.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 向消息队列发送有关订单的消息任务
 */
@Component
@EnableBinding(Source.class)
public class OrderQueueService {

    @Autowired
    Source source;

    public void addOrder(String tableName, Orders orders){
        OrderMessage<Orders> orderOrderMessage = new OrderMessage<>(tableName,
                MessageBehaviorConfig.ADD_MESSAGE, orders.getOrder_id(), orders);
        source.output().send(MessageBuilder.withPayload(orderOrderMessage).build());
    }

    public void deleteOrder(String tableName, String order_id){
        OrderMessage<String> orderOrderMessage = new OrderMessage<>(tableName,
                MessageBehaviorConfig.DELETE_MESSAGE, order_id,"");
        source.output().send(MessageBuilder.withPayload(orderOrderMessage).build());
    }

    public void updateOrder(String tableName, String order_id, Map<String,String> updateMap){
        OrderMessage<Map<String,String>> orderOrderMessage = new OrderMessage<>(tableName,
                MessageBehaviorConfig.UPDATE_MESSAGE, order_id,updateMap);
        source.output().send(MessageBuilder.withPayload(orderOrderMessage).build());
    }
}
