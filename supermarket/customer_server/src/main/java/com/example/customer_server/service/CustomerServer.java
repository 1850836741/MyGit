package com.example.customer_server.service;

import com.example.customer_server.entity.Customer;
import com.example.customer_server.entity.Order;
import com.example.customer_server.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class CustomerServer {

    @Autowired(required = false)
    CustomerMapper customerMapper;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/getVipNumber",method = RequestMethod.GET)
    public int getVipNumber(){
        return customerMapper.findVipNumber();
    }

    @RequestMapping(value = "/getCustomer",method = RequestMethod.GET)
    public List<Customer> getCustomer(){
        List<Customer> customers = customerMapper.findCustomerByNumber(20);
        return customers;
    }

    @RequestMapping(value = "/deleteCustomer",method = RequestMethod.GET)
    public void deleteCustomer(String customer_telephone_number){
        customerMapper.deleteCustomerById(customer_telephone_number);
    }

    @RequestMapping(value = "/addCustomer",method = RequestMethod.POST)
    public void addCustomer(Customer customer){
        customerMapper.addCustomer(customer);
    }
}
