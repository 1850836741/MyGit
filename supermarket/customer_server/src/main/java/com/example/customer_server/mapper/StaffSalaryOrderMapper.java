package com.example.customer_server.mapper;

import com.example.customer_server.entity.StaffSalaryOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StaffSalaryOrderMapper {

    /**
     * 查询对应ID工资账单
     * @param staff_order_id
     * @return
     */
    @Select("SELECT * FROM staff_salary_order WHERE staff_order_id = #{staff_order_id}")
    StaffSalaryOrder findStaffSalaryOrderById(@Param(value = "staff_order_id") String staff_order_id);
}
