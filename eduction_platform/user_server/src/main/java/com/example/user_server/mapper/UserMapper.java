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
    @SelectProvider(value = UserSqlProvider.class,method = "getUserById")
    User getUserById(@Param(value = "user_id") int user_id);

    /**
     * 查询登陆时需要的账号,密码,权限信息
     * @param user_id
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class,method = "getLogInInformation")
    User getLogInInformation(@Param(value = "user_id") int user_id);

    /**
     * 查询用户等级
     * @param user_id
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class,method = "getGradeById")
    short getGradeById(@Param(value = "user_id") int user_id);

    /**
     * 添加用户user
     * @param user
     */
    @SelectProvider(value = UserSqlProvider.class,method = "addUserBaseInformation")
    void addUserBaseInformation(User user);

    /**
     * 通过账号删除用户
     * @param user_id
     */
    @SelectProvider(value = UserSqlProvider.class,method = "deleteUser")
    void deleteUser(int user_id);

    /**
     * 修改用户信息
     * @param user
     */
    @SelectProvider(value = UserSqlProvider.class,method = "updateUser")
    void updateUser(User user);
}
