package com.example.customer_server.mapper;

import com.example.customer_server.entity.Staff;
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
}
