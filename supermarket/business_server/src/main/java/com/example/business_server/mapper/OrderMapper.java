package com.example.business_server.mapper;

import com.example.business_server.entity.Order;
import com.example.business_server.entity.OrderDetailed;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface OrderMapper {

    @Select("SELECT * FROM `order` WHERE order_id = #{order_id}")
    Order findOrderById(@Param(value = "order_id") String order_id);



    @Select("SELECT * FROM `order` LIMIT 0,#{number}")
    List<Order> findOrder(@Param(value = "number") int number);



    @Select("SELECT * FROM `order` WHERE order_time LIKE CONCAT('%',#{order_time},'%')")
    List<Order> findOrderWithTime(@Param(value = "order_time") String order_time);

    @Select("SELECT * FROM `order` WHERE order_purchaser = #{customer_telephone_number}")
    List<Order> findOrderByTelephone(@Param(value = "customer_telephone_number")String customer_telephone_number);

    @Select("SELECT * FROM orderDetailed WHERE order_time LIKE CONCAT('%',#{order_time},'%')")
    List<OrderDetailed> findOrderDetailedWithTime(@Param(value = "order_time") String order_time);

    @Select("SELECT * FROM orderDetailed WHERE order_id = #{order_id}")
    List<OrderDetailed> findOrderDetailedById(@Param(value = "order_id") String order_id);




    @Insert("INSERT INTO `order`(order_id, order_total_price, order_purchaser, order_time) " +
            "VALUES(#{order_id}, #{order_total_price}, #{order_purchaser}, #{order_time})")
    void addOrder(Order order);

    @Insert("INSERT INTO orderdetailed(order_id, order_goods_id, order_goods_name, order_subtotal, order_time) " +
            "VALUES(#{order_id}, #{order_goods_id}, #{order_goods_name}, #{order_subtotal}, #{order_time})")
    void addOrderInformation(OrderDetailed orderDetailed);




    @Delete("DELETE o,o_i FROM `order` o, orderdetailed o_i WHERE o.order_id = o_i.order_id AND o.order_id = #{order_id}")
    void deleteOrder(@Param("order_id") String order_id);
}
