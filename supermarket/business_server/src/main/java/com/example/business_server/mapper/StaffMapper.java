package com.example.business_server.mapper;

import com.example.business_server.entity.Staff;
import com.example.business_server.entity.StaffOrder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface StaffMapper {

    @Select("SELECT * FROM staff WHERE staff_order_id = #{staff_order_id}")
    @Results({
            @Result(id = true,column = "staff_order_id",property = "staff_order_id"),
            @Result(column = "staff_order_id",property = "staffOrders",
                    many = @Many(
                            select = "com.example.business_server.mapper.StaffMapper.findStaffOrderById",
                            fetchType = FetchType.LAZY
                    ))
    })
    Staff findStaffById(@Param(value = "staff_order_id") String staff_order_id);


    @Select("SELECT * FROM staff LIMIT 0,#{number}")
    List<Staff> findStaff(@Param(value = "number") int number);


    @Select("SELECT * FROM staff_order WHERE staff_order_id = #{staff_order_id}")
    List<StaffOrder> findStaffOrderById(@Param(value = "staff_order_id") String staff_order_id);

    @Select("SELECT * FROM staff_order WHERE staff_salary_time LIKE CONCAT('%',#{staff_salary_time},'%')")
    List<StaffOrder> findStaffOrderWithTime(@Param(value = "staff_salary_time") String staff_salary_time);

    @Insert("INSERT INTO staff(staff_id,staff_name,staff_idcard,staff_sex,staff_native,staff_nation,staff_position) VALUES(" +
            "#{staff_id},#{staff_name},#{staff_idcard},#{staff_sex},#{staff_native},#{staff_nation},#{staff_position})")
    void addStaff(Staff staff);

    @Insert("INSERT INTO staff_order(staff_order_id,staff_id,staff_salary,staff_salary_time) VALUES(" +
            "#{staff_order_id},#{staff_id},#{staff_salary},#{staff_salary_time})")
    void addStaffOrder(StaffOrder staffOrder);

    @Delete("DELETE FROM staff WHERE staff_id = #{staff_id}")
    void deleteStaff(@Param(value = "staff_id") String staff_id);

    @Delete("DELETE FROM staff_order WHERE staff_order_id = #{staff_order_id}")
    void deleteStaffOrder(@Param(value = "staff_order_id") String staff_order_id);
}
