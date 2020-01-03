package com.example.business_server;

import com.example.business_server.entity.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessServerApplicationTests {

    @Autowired
    RedisTemplate stringRedisTemplate;
    @Test
    public void contextLoads() {
        Map<String,Goods> map = new HashMap<>();
        Goods goods = new Goods();
        goods.setGoods_id(1);
        map.put("1",goods);
        stringRedisTemplate.opsForHash().putAll("test",map);

    }

}
