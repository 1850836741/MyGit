package com.example.customer_server.mapper;

import com.example.customer_server.entity.SourceOrder;
import com.example.customer_server.entity.SourceOrderInformation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 查询仓库货源信息
 */
public interface SourceOrderMapper {

    /**
     * 查询对应ID货源基本信息
     * @param source_order_id
     * @return
     */
    @Select("SELECT * FROM source_order WHERE source_order_id = #{source_order_id}")
    SourceOrder findSourceById(@Param(value = "source_order_id") int source_order_id);

    /**
     * 添加货源信息
     * @param sourceOrder
     */
    @Insert("INSERT INTO source_order(source_order_id, source_order_total_sum, source_order_time, source_order_place) " +
            "VALUES(#{source_order_id}, #{source_order_total_sum}, #{source_order_time}, #{source_order_place})")
    void addSource(SourceOrder sourceOrder);

    /**
     * 查询对应ID货源详细信息
     * @param source_order_id
     * @return
     */
    @Select("SELECT * FROM source_order_information WHERE source_order_id = #{source_order_id}")
    List<SourceOrderInformation> findSourceInformationById(@Param(value = "source_order_id") int source_order_id);

    /**
     * 添加货源小计信息
     * @param sourceOrderInformation
     */
    @Insert("INSERT INTO source_order_information (\n" +
            "\tsource_order_id,\n" +
            "\tsource_order_goods_id,\n" +
            "\tsource_order_goods_total_sum,\n" +
            "\tsource_order_goods_unitprice,\n" +
            "\tsource_order_goods_number\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t\t#{source_order_id}, #{source_order_goods_id}, #{source_order_goods_total_sum}, #{source_order_goods_unitprice}, #{source_order_goods_number})")
    void addSourceInformation(SourceOrderInformation sourceOrderInformation);

    /**
     * 删除对应ID货源所有信息
     * @param source_order_id
     */
    @Delete("DELETE s,\n" +
            " s_i\n" +
            "FROM\n" +
            "\tsource_order s,\n" +
            "\tsource_order_information s_i\n" +
            "WHERE\n" +
            "\ts.source_order_id = s_i.source_order_id\n" +
            "AND source_order_id = #{source_order_id}")
    void deleteSource(@Param(value = "source_order_id") int source_order_id);
}
