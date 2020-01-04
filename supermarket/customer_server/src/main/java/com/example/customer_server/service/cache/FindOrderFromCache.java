package com.example.customer_server.service.cache;

import com.alibaba.fastjson.JSON;
import com.example.customer_server.entity.Orders;
import com.example.customer_server.mapper.OrderMapper;
import com.example.customer_server.service.queue.OrderQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindOrderFromCache {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    OrderQueueService orderQueueService;

    @Autowired(required = false)
    OrderMapper orderMapper;

    public Orders findOrderById(String order_id){
        if (hasOrderById(order_id)){
           return JSON.parseObject((String) redisTemplate.opsForValue().get(order_id), Orders.class);
        }
        Orders orders = orderMapper.findOrderById(order_id);
        redisTemplate.opsForValue().set(order_id,JSON.toJSONString(orders));
        return orders;
    }

    public void addOrder(Orders orders){
        orderQueueService.addOrder("order", orders);
    }

    public void deleteOrder(String order_id){
        if (hasOrderById(order_id)){
            redisTemplate.delete(order_id);
        }
        orderQueueService.deleteOrder("order",order_id);
    }

    public void updateOrder(String order_id, Map<String,String> updateMap){
        if (hasOrderById(order_id)){
            redisTemplate.delete(order_id);
        }
        orderQueueService.updateOrder("order",order_id,updateMap);
    }

    public boolean hasOrderById(String order_id){
        return redisTemplate.hasKey(order_id);
    }
}
