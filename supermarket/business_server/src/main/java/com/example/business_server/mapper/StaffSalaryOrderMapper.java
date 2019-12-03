package com.example.business_server.mapper;

import com.example.business_server.entity.StaffSalaryOrder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StaffSalaryOrderMapper {

    /**
     * 查询对应ID工资账单
     * @param staff_order_id
     * @return
     */
    @Select("SELECT * FROM staff_salary_order WHERE staff_order_id = #{staff_order_id}")
    StaffSalaryOrder findStaffSalaryOrderById(@Param(value = "staff_order_id") String staff_order_id);

    /**
     * 查询一定数量的工资单
     * @param number
     * @return
     */
    @Select("SELECT * FROM staff_salary_order LIMIT 0,#{number}")
    List<StaffSalaryOrder> findSalaryOrderLimitNumber(@Param(value = "number") long number);

    /**
     * 查询一定时间内的工资表
     * @param staff_time
     * @return
     */
    @Select("SELECT * FROM staff_salary_order WHERE staff_time LIKE " + "#{staff_time}" +"'%'")
    List<StaffSalaryOrder> findSalaryOrderByTime(@Param(value = "staff_time") String staff_time);


    /**
     * 添加工资单
     * @param staffSalaryOrder
     */
    @Insert("INSERT INTO staff_salary_order(staff_salary,staff_expected_salary,staff_real_salary,staff_time,staff_order_id) " +
            "VALUES(#{staff_salary},#{staff_expected_salary},#{staff_real_salary},#{staff_time},#{staff_order_id})")
    void addSalaryOrder(@Param(value = "staffSalaryOrder") StaffSalaryOrder staffSalaryOrder);



    /**
     * 删除对应ID的员工工资单
     * @param staff_order_id
     */
    @Delete("DELETE FROM staff_salary_order WHERE staff_order_id = #{staff_order_id}")
    void deleteSalaryOrderById(@Param(value = "staff_order_id") String staff_order_id);
}
