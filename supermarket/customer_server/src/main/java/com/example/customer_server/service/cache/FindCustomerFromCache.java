package com.example.customer_server.service.cache;

import com.alibaba.fastjson.JSON;
import com.example.customer_server.entity.Customer;
import com.example.customer_server.mapper.CustomerMapper;
import com.example.customer_server.service.queue.CustomerQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindCustomerFromCache implements FindCache{

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired(required = false)
    CustomerMapper customerMapper;

    @Autowired
    CustomerQueueService customerQueueService;

    /**
     * 查找对应ID的顾客信息
     * @param customer_account
     * @return
     */
    public Customer findCustomerById(String customer_account){
        if (hasCustomerById(customer_account)){
            return JSON.parseObject((String) redisTemplate.opsForValue().get(customer_account),Customer.class);
        }
        Customer customer = customerMapper.findCustomerInformationById(customer_account);
        redisTemplate.opsForValue().set(customer.getCustomer_account(), JSON.toJSONString(customer));
        return customer;
    }

    /**
     * 删除对应ID的顾客信息
     * @param customer_account
     */
    public void deleteCustomerById(String customer_account){
        if (hasCustomerById(customer_account)){
            redisTemplate.delete(customer_account);
        }
    }

    /**
     * 添加顾客信息
     * @param customer
     */
    public void addCustomerById(Customer customer){
        redisTemplate.opsForValue().set(customer.getCustomer_account(), JSON.toJSONString(customer));
    }

    public void updateCustomerById(String customer_account){
        deleteCustomerById(customer_account);
    }

    /**
     * 是否有对应ID的顾客信息
     * @param customer_account
     * @return
     */
    public boolean hasCustomerById(String customer_account){
        return redisTemplate.hasKey(customer_account);
    }
}
