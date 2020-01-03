package com.example.business_server.mapper;

import com.example.business_server.entity.Source;
import com.example.business_server.entity.SourceDetailed;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface SourceMapper {

    @Select("SELECT * FROM source WHERE source_order_id = #{source_order_id}")
    @Results({
            @Result(id = true,column = "source_order_id",property = "source_order_id"),
            @Result(column = "source_order_id",property = "sourceDetaileds",
                    many = @Many(
                            select = "com.example.business_server.mapper.SourceMapper.findSourceDetailedById",
                            fetchType = FetchType.LAZY
                    ))
    })
    Source findSourceById(@Param(value = "source_order_id") String source_order_id);



    @Select("SELECT * FROM source LIMIT 0,#{number}")
    List<Source> findSource(@Param(value = "number") int number);


    @Select("SELECT * FROM source WHERE source_time LIKE CONCAT('%',#{source_time},'%'")
    List<Source> findSourceWithTime(@Param(value = "source_time") String source_time);


    @Select("SELECT * FROM sourcedetailed WHERE source_order_id = #{source_order_id}")
    List<SourceDetailed> findSourceDetailedById(@Param(value = "source_order_id") String source_order_id);

    @Insert("INSERT INTO source(source_order_id, source_total_price, source_of_supply, source_time) " +
            "VALUES(#{source_order_id}, #{source_total_price}, #{source_of_supply}, #{source_time})")
    void addSource(Source source);

    @Insert("INSERT INTO sourcedetailed(source_order_id, source_goods_id, source_goods_name, source_subtotal, source_time) " +
            "VALUES(#{source_order_id}, #{source_goods_id}, #{source_goods_name}, #{source_subtotal}, #{source_time})")
    void addSourceDetailed(SourceDetailed sourceDetailed);


    @Delete("DELETE s,s_d FROM source s,sourcedetailed s_d WHERE s.source_order_id = s_d.source_order_id AND " +
            "s.source_order_id = #{source_order_id}")
    void deleteSource(@Param(value = "source_order_id") String source_order_id);
}
