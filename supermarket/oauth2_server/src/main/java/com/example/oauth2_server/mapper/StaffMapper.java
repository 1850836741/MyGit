package com.example.oauth2_server.mapper;

import com.example.oauth2_server.entity.Staff;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface StaffMapper {

    @Select("SELECT * FROM staff WHERE staff_id = #{staff_id}")
    Staff findStaffById(@Param(value = "staff_id") int staff_id);


    @Select("SELECT * FROM staff LIMIT 0,#{number}")
    List<Staff> findStaff(@Param(value = "number") int number);


    @Insert("INSERT INTO staff(staff_id,staff_name,staff_idcard,staff_sex,staff_native,staff_nation,staff_position) VALUES(" +
            "#{staff_id},#{staff_name},#{staff_idcard},#{staff_sex},#{staff_native},#{staff_nation},#{staff_position})")
    void addStaff(Staff staff);


    @Delete("DELETE FROM staff WHERE staff_id = #{staff_id}")
    void deleteStaff(@Param(value = "staff_id") String staff_id);

    @Delete("DELETE FROM staff_order WHERE staff_order_id = #{staff_order_id}")
    void deleteStaffOrder(@Param(value = "staff_order_id") String staff_order_id);
}
