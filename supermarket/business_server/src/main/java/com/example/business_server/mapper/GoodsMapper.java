package com.example.business_server.mapper;

import com.example.business_server.entity.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 有关商品Dao层方法
 */
public interface GoodsMapper {

    @Select("SELECT * FROM goods LIMIT 0,#{number}")
    List<Goods> findGoodsWithNumber(@Param(value = "number") int number);

    @Select("SELECT * FROM goods WHERE goods_id = #{goods_id}")
    Goods findGoodsById(@Param(value = "goods_id") int goods_id);

    @Select("SELECT goods_price FROM goods WHERE goods_id = #{goods_id}")
    int findGoodsPriceById(@Param(value = "goods_id") int goods_id);

    @Select("SELECT * FROM goods WHERE goods_name LIKE CONCAT('%',#{goods_name},'%')")
    List<Goods> findGoodsByName(@Param(value = "goods_name") String goods_name);

    @Select("SELECT goods_number FROM goods WHERE goods_id = #{goods_id}")
    int findNumberById(@Param(value = "goods_id") int goods_id);

    @Select("SELECT * From goods")
    List<Goods> findAll();

    @Insert("INSERT INTO goods(goods_id, goods_name, goods_number, goods_original_price, goods_price, " +
            "goods_manufacture_data, goods_quality_time) " +
            "VALUES (#{goods_id}, #{goods_name}, #{goods_number}, #{goods_original_price}, #{goods_price}, #{goods_manufacture_data}, " +
            "#{goods_quality_time})")
    @Options(useGeneratedKeys = true,keyProperty = "goods_id",keyColumn = "goods_id")
    void addGoodsInformation(Goods goods);

    @Delete("DELETE FROM goods WHERE goods_id = #{goods_id}")
    void delete(@Param(value = "goods_id") int goods_id);
}
