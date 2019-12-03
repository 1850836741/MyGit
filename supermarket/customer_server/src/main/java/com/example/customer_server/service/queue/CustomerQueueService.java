package com.example.customer_server.service.queue;

import com.example.customer_server.entity.Customer;
import com.example.customer_server.entity.message.AddCustomerMessage;
import com.example.customer_server.entity.message.DeleteCustomerMessage;
import com.example.customer_server.entity.message.UpdateCustomerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用于向消息队列发送消息
 */
@Component
@EnableBinding(Source.class)
public class CustomerQueueService {


    @Autowired
    Source source;

    /**
     * 发送添加消息
     * @param tableName
     * @param customer
     */
    public void addCustomer(String tableName, Customer customer){
        AddCustomerMessage addCustomerMessage = new AddCustomerMessage(tableName,customer);
        source.output().send(MessageBuilder.withPayload(customer).build());
    }

    /**
     * 发送修改消息
     * @param tableName
     * @param customer_account
     * @param updateMap
     */
    public void Update(String tableName, String customer_account,Map<String,String> updateMap){
        UpdateCustomerMessage updateCustomerMessage = new UpdateCustomerMessage(tableName,customer_account,updateMap);
        source.output().send(MessageBuilder.withPayload(updateCustomerMessage).build());
    }

    /**
     * 发送删除消息
     * @param tableName
     * @param customer_account
     */
    public void deleteCustomer(String tableName, String customer_account){
        DeleteCustomerMessage deleteCustomerMessage = new DeleteCustomerMessage(tableName,customer_account);
        source.output().send(MessageBuilder.withPayload(deleteCustomerMessage).build());
    }
}
