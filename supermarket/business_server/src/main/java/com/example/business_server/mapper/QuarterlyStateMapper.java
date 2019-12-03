package com.example.business_server.mapper;

import com.example.business_server.entity.QuarterlyState;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 季度收纳支出表
 */
public interface QuarterlyStateMapper {

    /**
     * 查询对应ID的季度报表信息
     * @param quarterly_id
     * @return
     */
    @Select("SELECT * FROM quarterly_state WHERE quarterly_id = #{quarterly_id}")
    QuarterlyState findQuarterlyState(@Param(value = "quarterly_id") int quarterly_id);

    /**
     * 添加季度报表
     * @param quarterlyState
     */
    @Insert("INSERT INTO quarterly_state(quarterly_id, quarterly_pay, quarterly_income, quarterly_total_salary, quarterly_profit, quarterly_time) " +
            "VALUES(#{quarterly_id}, #{quarterly_pay}, #{quarterly_income}, #{quarterly_total_salary}, #{quarterly_profit}, #{quarterly_time})")
    void addQuarterlyState(QuarterlyState quarterlyState);

    /**
     * 删除对应ID的季度报表
     * @param quarterly_id
     */
    @Delete("DELETE FROM quarterly_state WHERE quarterly_id = #{quarterly_id}")
    void deleteQuarterlyState(@Param(value = "quarterly_id") int quarterly_id);
}
