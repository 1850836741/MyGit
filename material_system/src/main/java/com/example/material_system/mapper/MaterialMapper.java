package com.example.material_system.mapper;

import com.example.material_system.entity.Material;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaterialMapper {

    @Insert("INSERT INTO material(material_name,material_md5_id,material_upload_college,upload_time,material_upload_department) " +
            "VALUES(#{material_name},#{material_md5_id},#{material_upload_college},#{upload_time},#{material_upload_department})")
    void addMaterialMapper(Material material);

    @Select("SELECT material_name,material_md5_id,material_upload_college,upload_time,material_upload_department FROM material WHERE material_upload_department=#{material_upload_department} ORDER BY upload_time desc LIMIT #{start},#{end}")
    List<Material> getAllMaterialInformation(@Param(value = "start") int start, @Param(value = "end") int end,@Param(value = "material_upload_department") String material_upload_department);

    @Select("SELECT material_name,material_md5_id,material_upload_college,upload_time,material_upload_department FROM material " +
            "WHERE upload_time LIKE CONCAT('%',#{upload_time},'%') LIMIT #{start},#{end}")
    List<Material> getMaterialInformationByTime(@Param(value = "upload_time") String upload_time,@Param(value = "start") int start,@Param(value = "end") int end);

    @Select("SELECT material_md5_id FROM material WHERE material_md5_id = #{material_md5_id}")
    String judgeIsExist(@Param(value = "material_md5_id") String material_md5_id);

    @Select("SELECT material_name,material_md5_id,material_upload_college,upload_time,material_upload_department FROM material WHERE material_upload_college = #{material_upload_college} ORDER BY upload_time desc")
    List<Material> getMaterialInformationByCollegeName(@Param(value = "material_upload_college") String material_upload_college);

    @Select("SELECT material_name,upload_time FROM material WHERE material_md5_id = #{material_md5_id}")
    Material getMaterialNameAndTimeById(@Param(value = "material_md5_id") String material_md5_id);


    @Select("SELECT * FROM material WHERE material_md5_id = #{material_md5_id}")
    Material getMaterialById(@Param(value = "material_md5_id") String material_md5_id);

    @Select("SELECT COUNT(material_md5_id) FROM material")
    int count();

    @Delete("DELETE FROM material WHERE material_md5_id = #{material_md5_id}")
    void deleteMaterial(String material_md5_id);

    @Select("SELECT DISTINCT material_upload_department FROM material")
    List<String> getAllDepartmentName();

    @Select("SELECT * FROM material WHERE material_upload_department = #{material_upload_department} ORDER BY upload_time desc")
    List<Material> creatExcel(String material_upload_department);
}
