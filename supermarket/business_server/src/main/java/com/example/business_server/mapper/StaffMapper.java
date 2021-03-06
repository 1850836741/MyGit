package com.example.business_server.mapper;

import com.example.business_server.entity.Staff;
<<<<<<< HEAD
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
=======
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *操纵职工表
 */
public interface StaffMapper {

    /**
     * 查询职员详细信息
     * @param staff_id
     * @return
     */
    @Select("SELECT * FROM staff WHERE staff_id = #{staff_id}")
    Staff findStaffById(@Param(value = "staff_id") int staff_id);

    /**
     * 查询指定数量的员工
     * @param number
     * @return
     */
    @Select("SELECT * FROM staff LIMIT 0,50")
    List<Staff> findAnyStaff(@Param(value = "number") long number);


    /**
     * 添加职员信息
     * @param staff
     */
    @Insert("INSERT INTO staff (\n" +
            "\tstaff_id,\n" +
            "\tstaff_position,\n" +
            "\tstaff_name,\n" +
            "\tstaff_age,\n" +
            "\tstaff_sex,\n" +
            "\tstaff_idcard,\n" +
            "\tstaff_habitation,\n" +
            "\tstaff_native_place\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t\t#{staff_id}, #{staff_position}, #{staff_name}, #{staff_age}, #{staff_sex}, #{staff_idcard}, #{staff_habitation}, #{staff_native_place})")
    void addStaff(Staff staff);

    /**
     * 删除对应ID的职员信息
     * @param staff_id
     */
    @Delete("DELETE FROM staff WHERE staff_id = #{staff_id}")
    void deleteStaff(@Param(value = "staff_id") int staff_id);

    /**
     * 修改对应ID职员职位
     * @param staff_id
     * @param staff_position
     */
    @Update("UPDATE staff SET staff_position = #{staff_position} WHERE staff_id = #{staff_id}")
    void setStaffPosition(@Param(value = "staff_id") int staff_id, @Param(value = "staff_position") String staff_position);

    /**
     * 修改对应ID职员居住地
     * @param staff_id
     * @param staff_habitation
     */
    @Update("UPDATE staff SET staff_habitation = #{staff_habitation} WHERE staff_id = #{staff_id}")
    void setStaffHabitation(@Param(value = "staff_id") int staff_id, @Param(value = "staff_habitation") String staff_habitation);
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
