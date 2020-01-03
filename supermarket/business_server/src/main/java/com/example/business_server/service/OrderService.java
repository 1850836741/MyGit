package com.example.business_server.service;

import com.example.business_server.entity.Order;
import com.example.business_server.entity.OrderDetailed;
import com.example.business_server.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrderService {

    @Autowired(required = false)
    OrderMapper orderMapper;

    @RequestMapping(value = "/getOrder",method = RequestMethod.GET)
    public List<Order> getOrder(){
        return orderMapper.findOrder(20);
    }

    @RequestMapping(value = "/getOrderById",method = RequestMethod.GET)
    public Order getOrderById(String order_id){
        return orderMapper.findOrderById(order_id);
    }

    @RequestMapping(value = "/getOrderWithTime",method = RequestMethod.GET)
    public List<Order> getOrderWithTime(String order_time){
        return orderMapper.findOrderWithTime(order_time);
    }

    @RequestMapping(value = "/getOrderDetailedWithTime",method = RequestMethod.GET)
    public List<OrderDetailed> getOrderDetailedWithTime(String order_time){
        return orderMapper.findOrderDetailedWithTime(order_time);
    }

    @RequestMapping(value = "/getOrderDetailedById",method = RequestMethod.GET)
    public List<OrderDetailed> getOrderDetailedById(String order_id){
        return orderMapper.findOrderDetailedById(order_id);
    }

    @RequestMapping(value = "/addOrder",method = RequestMethod.POST)
    public void addOrder(@RequestBody List<OrderDetailed> orderDetailedList, String order_purchaser){
        String order_id = UUID.randomUUID().toString();
        int order_total_price = 0;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i =0;i<orderDetailedList.size();i++){
            OrderDetailed orderDetailed = orderDetailedList.get(i);
            orderDetailed.setOrder_id(order_id);
            orderDetailed.setOrder_time(simpleDateFormat.format(date));
            order_total_price += orderDetailed.getOrder_subtotal();
            orderMapper.addOrderInformation(orderDetailed);
        }
        Order order = new Order();
        order.setOrder_id(order_id);
        order.setOrder_purchaser(order_purchaser);
        order.setOrder_total_price(order_total_price);
        order.setOrder_time(simpleDateFormat.format(date));
        orderMapper.addOrder(order);
    }

    @RequestMapping(value = "/deleteOrder",method = RequestMethod.GET)
    public void deleteOrder(String order_id){
        orderMapper.deleteOrder(order_id);
    }

    @RequestMapping(value = "/getSalesVolume",method = RequestMethod.GET)
    public int getSalesVolume(){
        int totalSales = 0;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sales = simpleDateFormat.format(date).substring(0,10);
        List<Order> list = orderMapper.findOrderWithTime(sales);
        for (int i = 0;i < list.size(); i++){
            totalSales += list.get(i).getOrder_total_price();
        }
        return totalSales;
    }

    @RequestMapping(value = "/getOrderByTelephone",method = RequestMethod.GET)
    public List<Order> getOrderByTelephone(int customer_telephone_number){
        return orderMapper.findOrderByTelephone(String.valueOf(customer_telephone_number));
    }
}
