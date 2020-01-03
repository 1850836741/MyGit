package com.example.business_server.service;

import com.alibaba.fastjson.JSON;
import com.example.business_server.entity.Goods;
import com.example.business_server.mapper.GoodsMapper;
import com.example.business_server.safelock.LockConfig;
import com.example.business_server.safelock.ZookeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class GoodsService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired(required = false)
    GoodsMapper goodsMapper;

    @Autowired
    ZookeeperService zookeeperService;

    @RequestMapping(value = "/getGoodsList",method = RequestMethod.GET)
    public List<Goods> getGoodsList(){
        return goodsMapper.findGoodsWithNumber(20);
    }

    @RequestMapping(value = "/getGoodsById",method = RequestMethod.GET)
    public Goods getGoodsById(int goods_id){
        if (stringRedisTemplate.hasKey(String.valueOf(goods_id))){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LockConfig.GOODS_LOCK_PATH).append(goods_id);
            String lockName = zookeeperService.addReadLock(stringBuilder.toString());
            Goods goods = JSON.parseObject(stringRedisTemplate.opsForValue().get(goods_id),Goods.class);
            zookeeperService.deleteLock(lockName);
            return goods;
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LockConfig.GOODS_LOCK_PATH).append(goods_id);
            String lockName = zookeeperService.addReadLock(stringBuilder.toString());
            Goods goods = goodsMapper.findGoodsById(goods_id);
            stringRedisTemplate.opsForValue().set(String.valueOf(goods_id),JSON.toJSONString(goods));
            zookeeperService.deleteLock(lockName);
            return JSON.parseObject(stringRedisTemplate.opsForValue().get(goods_id),Goods.class);
        }
    }

    @RequestMapping(value = "/getGoodsPriceById",method = RequestMethod.GET)
    public int getGoodsPriceById(int goods_id){
        if (stringRedisTemplate.hasKey(String.valueOf(goods_id))){
            return JSON.parseObject(stringRedisTemplate.opsForValue().get(String.valueOf(goods_id)),Goods.class).getGoods_price();
        }else {
            return goodsMapper.findGoodsPriceById(goods_id);
        }
    }

    @RequestMapping(value = "/getGoodsByName",method = RequestMethod.GET)
    public List<Goods> getGoodsByName(String goods_name){
        return goodsMapper.findGoodsByName(goods_name);
    }


    @RequestMapping(value = "/addGoods",method = RequestMethod.POST)
    public void addGoods(Goods goods){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LockConfig.GOODS_LOCK_PATH).append(goods.getGoods_id());
        String LockName = zookeeperService.addWriteLock(stringBuilder.toString());
        if (stringRedisTemplate.hasKey(String.valueOf(goods.getGoods_id()))){
            stringRedisTemplate.delete(String.valueOf(goods.getGoods_id()));
        }
        goodsMapper.addGoodsInformation(goods);
        stringRedisTemplate.opsForValue().set(String.valueOf(goods.getGoods_id()),JSON.toJSONString(goods));
        zookeeperService.deleteLock(LockName);
    }


    @RequestMapping(value = "/deleteGoods",method = RequestMethod.GET)
    public void deleteGoods(int goods_id){
        if (stringRedisTemplate.hasKey(String.valueOf(goods_id))){
            stringRedisTemplate.delete(String.valueOf(goods_id));
        }
        goodsMapper.delete(goods_id);
    }


    @RequestMapping(value = "/getExpireGoods",method = RequestMethod.GET)
    public List<Goods> getExpireGoods(){
        List<Goods> goodsList = goodsMapper.findAll();
        List<Goods> expireGoods = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        goodsList.stream().forEach(x->{
            try {
                if ((date.getTime()-simpleDateFormat.parse(x.getGoods_manufacture_data()).getTime())/1000/60/60/24
                        > x.getGoods_quality_time()*30/3){
                    expireGoods.add(x);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return expireGoods;
    }

    @RequestMapping(value = "/getHotGoods",method = RequestMethod.GET)
    public String getHotGoods(){
        return "java从入门到放弃";
    }


    public void scan() throws IOException {
        Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash()
                .scan("", ScanOptions.scanOptions().match("*").count(100).build());
        while(cursor.hasNext()){
            Object key = cursor.next().getKey();
            Object valueSet = cursor.next().getValue();
        }
        cursor.close();
    }

}
