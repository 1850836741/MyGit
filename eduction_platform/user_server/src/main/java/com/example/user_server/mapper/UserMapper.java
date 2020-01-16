package com.example.user_server.mapper;

import com.example.user_server.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 操纵User信息的Dao层接口
 */
public interface UserMapper {

    /**
     * 查询对应Id的所有属性
     * @param user_id
     * @return
     */
    @Select("SELECT * FROM user_table WHERE user_id = #{user_id}")
    User getUserById(@Param(value = "user_id") int user_id);

    /**
     * 查询登陆时需要的账号,密码,权限信息
     * @param user_id
     * @return
     */
    @Select("SELECT user_id, password, jurisdiction FROM user_table WHERE user_id = #{user_id}")
    User getLogInInformation(@Param(value = "user_id") int user_id);

    /**
     * 查询用户等级
     * @param user_id
     * @return
     */
    @Select("SELECT user_id, password, jurisdiction FROM user_table WHERE user_id = #{user_id}")
    short getGradeById(@Param(value = "user_id") int user_id);

    /**
     * 添加用户user
     * @param user
     */
    @Insert("INSET INTO user_table(user_id,password,name,sex,cell_phone,grade,jurisdiction) " +
            "VALUES(#{user_id},#{password},#{name},#{sex},#{cell_phone},#{grade},#{jurisdiction})")
    void addUser(User user);

}
