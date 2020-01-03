package com.example.customer_server.mapper;

import com.example.customer_server.entity.Customer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消费者Dao层方法
 */
public interface CustomerMapper {

    @Select("SELECT COUNT(*) FROM customer WHERE vip_rank LIKE CONCAT('%','会员','%')")
    int findVipNumber();

    @Select("SELECT * FROM customer LIMIT 0,#{number}")
    List<Customer> findCustomerByNumber(@Param(value = "number") int number);

    @Insert("INSERT INTO customer(customer_telephone_number,customer_name,vip_rank) VALUES(#{customer_telephone_number},#{customer_name},#{vip_rank})")
    void addCustomer(Customer customer);

    @Delete("DELETE FROM customer WHERE customer_telephone_number = #{customer_telephone_number}")
    void deleteCustomerById(@Param(value = "customer_telephone_number")String customer_telephone_number);
}
