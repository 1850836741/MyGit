package com.example.customer_server.mapper;

import com.example.customer_server.entity.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 查询商品信息
 */
public interface GoodsMapper {

    /**
     * 查询对应ID商品基本信息
     * @param goods_id
     * @return
     */
    @Select("SELECT * FROM goods WHERE goods_id = #{goods_id};")
    Goods findGoodsById(@Param(value = "goods_id") int goods_id);

    /**
     * 查询对应ID商品所有信息
     * @param goods_id
     * @return
     */
    @Select("SELECT\n" +
            "\tgoods.goods_price,\n" +
            "\tgoods_information.*\n" +
            "FROM\n" +
            "\tgoods,\n" +
            "\tgoods_information\n" +
            "WHERE\n" +
            "\tgoods.goods_id = goods_information.goods_id\n" +
            "AND goods.goods_id = #{goods_id};")
    Goods findGoodsInformationById(@Param(value = "goods_id") int goods_id);

    /**
     * 查找对应名字的商品
     * @param goods_name
     * @return
     */
    @Select("SELECT goods.goods_price,goods_information.* FROM goods,goods_information WHERE goods.goods_id = goods_information.goods_id " +
            "AND goods_information.goods_name = #{goods_name}")
    List<Goods> findGoodsInformationByName(@Param(value = "goods_name") String goods_name);

    /**
     * 添加商品基本信息
     * @param goods
     */
    @Insert("INSERT INTO goods (goods_id, goods_price) VALUES (#{goods_id}, #{goods_price});")
    void addGoods(Goods goods);

    /**
     * 添加商品详细信息
     * @param goods
     */
    @Insert("INSERT INTO goods_information(goods_id, goods_name, goods_number, goods_originalprice, goods_source) " +
            "VALUES (#{goods_id}, #{goods_name}, #{goods_number}, #{goods_originalprice}, #{goods_source})")
    void addGoodsInformation(Goods goods);

    @Select("SELECT\n" +
            "\tgoods.goods_price,\n" +
            "\tgoods_information.*\n" +
            "FROM\n" +
            "\tgoods,\n" +
            "\tgoods_information\n" +
            "WHERE\n" +
            "\tgoods.goods_id = goods_information.goods_id\n" +
            "LIMIT 0,\n" +
            " #{number}")
    List<Goods> findGoodsLimitNumber(@Param(value = "number") long number);
}
