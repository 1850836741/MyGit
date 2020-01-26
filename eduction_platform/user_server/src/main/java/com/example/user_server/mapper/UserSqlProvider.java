package com.example.user_server.mapper;

import com.example.user_server.entity.User;
import com.example.user_server.r_cache.RedisCacheConfig;
import org.apache.ibatis.jdbc.SQL;

/**
 * 操作User的动态SQL语句
 */
public class UserSqlProvider {

    /**
     * 查询对应Id的所有属性
     * @param user_id
     * @return
     */
    public String getUserById(int user_id){
        return new SQL(){
            {
                SELECT("*");
                FROM("user_table");
                StringBuilder stringBuilder = new StringBuilder("user_id");
                if (user_id != 0){
                    stringBuilder.append(" = #{user_id}");
                    WHERE(stringBuilder.toString());
                }
            }
        }.toString();
    }

    /**
     * 查询登陆时需要的账号,密码,权限信息
     * @param user_id
     * @return
     */
    public String getLogInInformation(int user_id){
        return new SQL(){
            {
                SELECT("user_id, password, jurisdiction");
                FROM("user_table");
                StringBuilder stringBuilder = new StringBuilder("user_id");
                if (user_id != 0){
                    stringBuilder.append(" = #{user_id}");
                    WHERE(stringBuilder.toString());
                }
            }
        }.toString();
    }

    /**
     * 查询用户等级
     * @param user_id
     * @return
     */
    public String getGradeById(int user_id){
        return new SQL(){
            {
                SELECT("grade");
                FROM("user_table");
                StringBuilder stringBuilder = new StringBuilder("user_id");
                if (user_id != 0){
                    stringBuilder.append(" = #{user_id}");
                    WHERE(stringBuilder.toString());
                }
            }
        }.toString();
    }

    /**
     * 添加用户user
     * @param user
     * @return
     */
    public String addUserBaseInformation(User user){
        return new SQL(){
            {
                INSERT_INTO("user_table");
                if (user.getUser_id()>=100000 && user.getUser_id()<Integer.MAX_VALUE){
                    VALUES("user_id","#{user_id}");
                }
                if (user.getPassword().length()<=20 && user.getPassword().length()>0){
                    VALUES("password","#{password}");
                }
                if (user.getJurisdiction()>=RedisCacheConfig.JURISDICTION_MIN
                        && user.getJurisdiction()<=RedisCacheConfig.JURISDICTION_MAX){
                    VALUES("jurisdiction","#{jurisdiction}");
                }
            }
        }.toString();
    }


    /**
     * 根据账户删除用户
     * @param user_id
     * @return
     */
    public String deleteUser(int user_id){
        return new SQL(){
            {
                StringBuilder stringBuilder = new StringBuilder("user_id = ");
                DELETE_FROM("user_table");
                if (user_id>100000 && user_id<Integer.MAX_VALUE){
                    stringBuilder.append("#{user_id}");
                    WHERE(stringBuilder.toString());
                }
            }
        }.toString();
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    public String updateUser(User user){
        return new SQL(){
            {
                UPDATE("user_table");
                if (user.getPassword() != null
                        && user.getPassword().length() <= 20
                        && user.getPassword().length()>0){
                    SET("password = #{password}");
                }

                if (user.getName() != null){
                    SET("name = #{name}");
                }

                if (user.getCell_phone().length() == 11){
                    SET("cell_phone = #{cell_phone}");
                }

                if (user.getGrade() >= RedisCacheConfig.GRADE_MIN && user.getGrade() <= RedisCacheConfig.GRADE_MAX){
                    SET("grade = #{grade}");
                }

                SET("sex = ".concat(String.valueOf(user.getSex().get(0))));

                if (user.getUser_id()>=100000 && user.getUser_id()<Integer.MAX_VALUE){
                    WHERE("user_id = #{user_id}");
                }
            }
        }.toString();
    }
}
