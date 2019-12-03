package com.example.customer_server.service.queue;

import com.example.customer_server.entity.Goods;
import com.example.customer_server.entity.message.GoodsMessage;
import com.example.customer_server.entity.message.MessageBehaviorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 向消息队列发送有关商品的消息任务
 */
@Component
@EnableBinding(Source.class)
public class GoodsQueueService {

    @Autowired
    Source source;

    /**
     * 发送添加消息
     * @param tableName
     * @param goods
     */
    public void addGoods(String tableName, Goods goods){
        GoodsMessage<Goods> goodsGoodsMessage = new GoodsMessage<>(tableName,
                MessageBehaviorConfig.ADD_MESSAGE, goods.getGoods_id(), goods);
        source.output().send(MessageBuilder.withPayload(goodsGoodsMessage).build());
    }

    /**
     * 发送删除消息
     * @param tableName
     * @param goods_id
     */
    public void deleteGoods(String tableName, int goods_id){
        GoodsMessage<String> goodsGoodsMessage = new GoodsMessage<>(tableName,
                MessageBehaviorConfig.DELETE_MESSAGE, goods_id, "");
        source.output().send(MessageBuilder.withPayload(goodsGoodsMessage).build());
    }

    /**
     * 发送修改消息
     * @param tableName
     * @param goods_id
     * @param updateMap
     */
    public void updateGoods(String tableName,int goods_id,Map<String,String> updateMap){
        GoodsMessage<Map<String,String>> goodsGoodsMessage = new GoodsMessage<>(tableName,
                MessageBehaviorConfig.UPDATE_MESSAGE, goods_id, updateMap);
        source.output().send(MessageBuilder.withPayload(goodsGoodsMessage).build());
    }
}
