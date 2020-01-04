package com.example.customer_server.service.cache;

import com.alibaba.fastjson.JSON;
import com.example.customer_server.Unit.CloseUnit;
import com.example.customer_server.entity.Goods;
import com.example.customer_server.mapper.GoodsMapper;
import com.example.customer_server.service.queue.GoodsQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对商品缓存操作
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindGoodsFromCache implements FindCache{

    @Autowired
    RedisTemplate redisTemplate;


    @Autowired(required = false)
    GoodsMapper goodsMapper;

    @Autowired
    GoodsQueueService goodsQueueService;

    /**
     *获取50个热点key
     * @return
     */
    public Map<String,Goods> findAllHotGoods(long number){
        Set<String> hotSet = redisTemplate.opsForSet().distinctRandomMembers("hot",number);
        Map<String,Goods> goodsMap = new HashMap<>();
        for (String goods_id : hotSet){
            goodsMap.put(goods_id,JSON.parseObject((String) redisTemplate.opsForValue().get(goods_id),Goods.class));
        }
        if (goodsMap.size()<1){
            for (Goods goods : goodsMapper.findGoodsLimitNumber(number)){
                goodsMap.put(String.valueOf(goods.getGoods_id()),goods);
            }
        }
        return goodsMap;
    }

    /**
     * 查询对应名字的商品
     * @param goods_name
     * @return
     */
    public Map<String,Goods> findGoodsByName(String goods_name){
        Map<String,Goods> goodsMap = new HashMap<>();
        Cursor<Map.Entry<String,String>> cursor = redisTemplate.opsForHash().scan("goodsHash",
                ScanOptions.scanOptions().match(goods_name).count(100).build());
        while (cursor.hasNext()){
            Goods goods = JSON.parseObject(cursor.next().getValue(),Goods.class);
            goodsMap.put(String.valueOf(goods.getGoods_id()),goods);
        }
        CloseUnit.Close(cursor);
        if (goodsMap.size() < 1){
            List<Goods> list = goodsMapper.findGoodsInformationByName(goods_name);
            for (Goods goods : list){
                addGoods(goods);
                goodsMap.put(String.valueOf(goods.getGoods_id()),goods);
            }
        }
        return goodsMap;
    }


    /**
     * 查询对应ID的商品，如果没有，就从数据库查
     * @param goods_id
     * @return
     */
    public Goods findGoodsById(int goods_id){

        if (hasGoodsById(goods_id)){
            return JSON.parseObject((String) redisTemplate.opsForValue().get(goods_id),Goods.class);
        }
        Goods goods = goodsMapper.findGoodsInformationById(goods_id);
        addGoods(goods);
        return goods;
    }

    /**
     * 向缓存中添加商品
     * @param goods
     */
    public void addGoods(Goods goods){
        redisTemplate.opsForValue().set(goods.getGoods_id(),JSON.toJSONString(goods));
        redisTemplate.opsForHash().put("goodsHash",goods.getGoods_name(),JSON.toJSONString(goods));
    }

    public void deleteGoods(int goods_id){
        if (hasGoodsById(goods_id)){
            redisTemplate.delete(goods_id);
            redisTemplate.opsForHash().delete("goodsHash",goods_id);
        }
    }

    public void updateGoods(int goods_id){
        if (hasGoodsById(goods_id)){
            redisTemplate.delete(goods_id);
            redisTemplate.opsForHash().delete("goodsHash",goods_id);
        }
    }
    /**
     * 是否有对应ID商品信息
     * @param goods_id
     * @return
     */
    public boolean hasGoodsById(int goods_id){
        return redisTemplate.hasKey(goods_id);
    }
}
