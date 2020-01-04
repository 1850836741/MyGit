package com.example.customer_server.mapper;

import com.example.customer_server.entity.Customer;
<<<<<<< HEAD
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消费者Dao层方法
 */
public interface CustomerMapper {

    @Select("SELECT COUNT(*) FROM customer WHERE vip_rank LIKE CONCAT('%','会员','%')")
    int findVipNumber();

    @Select("SELECT * FROM customer LIMIT 0,#{number}")
    List<Customer> findCustomerByNumber(@Param(value = "number") int number);

    @Insert("INSERT INTO customer(customer_telephone_number,customer_name,vip_rank) VALUES(#{customer_telephone_number},#{customer_name},#{vip_rank})")
    void addCustomer(Customer customer);

    @Delete("DELETE FROM customer WHERE customer_telephone_number = #{customer_telephone_number}")
    void deleteCustomerById(@Param(value = "customer_telephone_number")String customer_telephone_number);
=======
import org.apache.ibatis.annotations.*;

/**
 * 查询顾客信息
 */
public interface CustomerMapper {

    /**
     * 查询对应ID的顾客基本信息
     * @param customer_account
     * @return
     */
    @Select("select * from customer where customer_account = #{customer_account}")
    Customer findCustomerById(@Param(value = "customer_account") String customer_account);


    /**
     * 查询对应ID的顾客所有信息
     * @param customer_account
     * @return
     */
    @Select("SELECT\n" +
            "\tcustomer.customer_password,\n" +
            "\tcustomer.customer_role,\n" +
            "\tcustomer.registration_time,\n" +
            "  customer.is_vip,\n" +
            "\tcustomer_information.*\n" +
            "FROM\n" +
            "\tcustomer\n" +
            "RIGHT JOIN customer_information ON customer.customer_account = customer_information.customer_account\n" +
            "AND customer.customer_account = #{customer_account}")
    Customer findCustomerInformationById(@Param(value = "customer_account") String customer_account);

    /**
     * 添加新用户
     * @param customer
     */
    @Insert("INSERT INTO customer (\n" +
            "\tcustomer_account,\n" +
            "\tcustomer_password,\n" +
            "\tcustomer_role,\n" +
            "\tregistration_time,\n" +
            "\tis_vip\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t\t#{customer_account},#{customer_password},#{customer_role},#{registration_time},#{is_vip})")
    void addCustomer(Customer customer);


    /**
     * 修改对应ID顾客的名字
     * @param customer_account
     * @param customer_name
     */
    @Update("UPDATE customer_information SET customer_name = #{customer_name} WHERE customer_account = #{customer_account}")
    void setCustomerName(@Param(value = "customer_account") String customer_account, @Param(value = "customer_name") String customer_name);

    /**
     * 修改对应ID顾客的年龄
     * @param customer_account
     * @param customer_age
     */
    @Update("UPDATE customer_information SET customer_age = #{customer_age} WHERE customer_account = #{customer_account}")
    void setCustomerAge(@Param(value = "customer_account") String customer_account, @Param(value = "customer_age") int customer_age);

    /**
     * 修改对应ID顾客的性别
     * @param customer_account
     * @param customer_sex
     */
    @Update("UPDATE customer_information SET customer_sex = #{customer_sex} WHERE customer_account = #{customer_account}")
    void setCustomerSex(@Param(value = "customer_account") String customer_account, @Param(value = "customer_sex") String customer_sex);

    /**
     * 修改对应ID顾客的电话号码
     * @param customer_account
     * @param customer_telephone
     */
    @Update("UPDATE customer_information SET customer_telephone = #{customer_telephone} WHERE customer_account = #{customer_account}")
    void setCustomerTelephone(@Param(value = "customer_account") String customer_account, @Param(value = "customer_telephone") String customer_telephone);

    /**
     * 修改对应ID顾客的密码
     * @param customer_account
     * @param customer_password
     */
    @Update("UPDATE customer_information SET customer_password = #{customer_password} WHERE customer_account = #{customer_account}")
    void setCustomerPassword(@Param(value = "customer_account") String customer_account, @Param(value = "customer_password") String customer_password);

    /**
     * 修改对应ID顾客的vip标志
     * @param customer_account
     * @param is_vip
     */
    @Update("UPDATE customer_information SET is_vip = #{is_vip} WHERE customer_account = #{customer_account}")
    void setCustomerVip(@Param(value = "customer_account") String customer_account, @Param(value = "customer_password") byte is_vip);

    /**
     * 删除指定用户
     * @param customer_account
     */
    @Delete("DELETE c,\n" +
            " c_i\n" +
            "FROM\n" +
            "\tcustomer c,\n" +
            "\tcustomer_information c_i\n" +
            "WHERE\n" +
            "\tc.customer_account = c_i.customer_account\n" +
            "AND c.customer_account = #{customer_account}")
    void deleteCustomer(@Param(value = "customer_account") String customer_account);

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
