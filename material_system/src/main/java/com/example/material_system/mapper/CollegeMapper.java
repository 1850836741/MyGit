package com.example.material_system.mapper;

import com.example.material_system.entity.College;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollegeMapper {

    @Select("SELECT college_account,college_password,college_authority FROM college WHERE college_account = #{college_account}")
    College getCollegeLoginInformation(@Param(value = "college_account") int college_account);

    @Select("SELECT college_account,college_password,college_authority,college_name FROM college WHERE college_account = #{college_account}")
    College getCollegeAllInformation(@Param(value = "college_account") int college_account);

    @Select("SELECT college_account,college_password,college_authority,college_name FROM college WHERE college_account != #{college_account}")
    List<College> getAllCollege(int college_account);

    @Select("SELECT DISTINCT college_name FROM college")
    List<String> getAllCollegeName();

    @Insert("INSERT INTO college(college_name,college_account,college_password,college_authority) " +
            "VALUES(#{college_name},#{college_account},#{college_password},#{college_authority})")
    void addCollege(College college);

    @Select("DELETE FROM college WHERE college_account = #{college_account}")
    void deleteCollege(@Param(value = "college_account") int college_account);
}
