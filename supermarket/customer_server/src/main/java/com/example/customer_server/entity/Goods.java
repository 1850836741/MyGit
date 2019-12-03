package com.example.customer_server.entity;

import lombok.Data;

import java.util.Objects;

@Data
public class Goods {
    private int goods_id;
    private int goods_price;
    private String goods_name;
    private int goods_number;
    private int goods_originalprice;
    private String goods_source;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return goods_id == goods.goods_id &&
                goods_price == goods.goods_price &&
                goods_number == goods.goods_number &&
                goods_originalprice == goods.goods_originalprice &&
                goods_name.equals(goods.goods_name) &&
                goods_source.equals(goods.goods_source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goods_id, goods_price, goods_name, goods_number, goods_originalprice, goods_source);
    }

}
