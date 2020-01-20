package com.example.oauth2_server.mapper;

import com.example.oauth2_server.entity.User;
import com.example.oauth2_server.r_cache.RedisCacheConfig;
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
                if (user.getJurisdiction().length()>= RedisCacheConfig.JURISDICTION_MIN){
                    VALUES("jurisdiction","#{jurisdiction}");
                }
            }
        }.toString();
    }
}
