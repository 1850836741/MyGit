package com.example.business_server.mapper;

import com.example.business_server.entity.Order;
import com.example.business_server.entity.OrderInformation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 查询订单信息
 */
public interface OrderMapper {

    /**
     * 查询对应ID订单总计信息
     * @param order_id
     * @return
     */
    @Select("SELECT * FROM order WHERE order_id = #{order_id}")
    Order findOrderById(@Param("order_id") String order_id);

    /**
     * 查询number数量的订单
     * @param number
     * @return
     */
    @Select("SELECT * FROM order LIMIT 0,#number")
    List<Order> findAnyOrder(@Param("number") long number);

    /**
     * 添加对应ID订单总计信息
     * @param order
     */
    @Insert("INSERT INTO order(order_id, order_purchaser_account, order_total_sum, order_time) " +
            "VALUES(#{order_id}, #{order_purchaser_account}, #{order_total_sum}, #{order_time})")
    void addOrder(Order order);

    /**
     * 查询对应ID订单小计
     */
    @Select("SELECT * FROM order_information WHERE order_id = #{order_id}")
    List<OrderInformation> findOrderInformationById(@Param("order_id") String order_id);

    /**
     * 添加对应ID订单小计信息
     * @param orderInformation
     */
    @Insert("INSERT INTO order_information(order_id, order_goods_id, order_goods_total_sum, order_goods_unitprice, order_goods_number) " +
            "VALUES(#{order_id}, #{order_goods_id}, #{order_goods_total_sum}, #[order_goods_unitprice], #{order_goods_number})")
    void addOrderInformation(OrderInformation orderInformation);

    /**
     * 删除对应ID订单所有信息
     * @param order_id
     */
    @Delete("DELETE o,o_i FROM order o, order_information o_i WHERE o.order_id = o_i.order_id AND o.order_id = #{order_id}")
    void deleteOrder(@Param("order_id") String order_id);

}
