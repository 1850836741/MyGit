package com.example.business_server;

<<<<<<< HEAD
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

=======
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessServerApplicationTests {

<<<<<<< HEAD
    @Autowired
    RedisTemplate stringRedisTemplate;
    @Test
    public void contextLoads() {
        Map<String,Goods> map = new HashMap<>();
        Goods goods = new Goods();
        goods.setGoods_id(1);
        map.put("1",goods);
        stringRedisTemplate.opsForHash().putAll("test",map);

=======
    @Test
    public void contextLoads() {
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    }

}
