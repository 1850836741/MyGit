package com.example.user_server.r_cache;

import com.alibaba.fastjson.JSON;
import com.example.user_server.tool.BloomFilter;
import org.apache.curator.shaded.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 操作redis缓存的工具类,UserHash为存储用户信息的ID
 */
public class RedisTool<T> {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    BloomFilter bloomFilter;

    /**
     * 向缓存中添加key-value
     * @param key
     * @param value
     */
    public void addObject(String key,T value){
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(value));
    }

    /**
     * 向缓存中添加key-value并设置过期时间
     * @param timeout
     */
    public void addObjectWithLimit(String key, T value, int timeout, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(value),timeout,timeUnit);
    }

    /**
     * 返回查询对应key的对象
     * @param key
     * @param ObjectClass
     * @return
     */
    public T selectObject(String key, Class<T> ObjectClass){
        T value = JSON.parseObject(stringRedisTemplate.opsForValue().get(key),ObjectClass);
        return value;
    }

    /**
     * 向缓存中的Hash添加key-value
     * @param hashKey
     * @param key
     * @param value
     */
    public void addToHash(String hashKey, String key, T value){
        stringRedisTemplate.opsForHash().put(hashKey, key, JSON.toJSONString(value));
    }

    /**
     * 删除Hash中的对应Id的值
     * @param hashKey
     * @param key
     */
    public void deleteWithHash(String hashKey, String key){
        stringRedisTemplate.opsForHash().delete(hashKey,key);
    }

    /**
     * 查询Hash中的对应Id的值
     * @param hashKey
     * @param key
     * @param ObjectClass
     * @return
     */
    public T selectUserByHash(String hashKey, String key, Class<T> ObjectClass){
        T value = JSON.parseObject(String.valueOf(stringRedisTemplate.opsForHash().get(hashKey,key)),ObjectClass);
        return value;
    }

    /**
     * 删除缓存中对应key的对象
     * @param key
     */
    public void deleteCacheByKey(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 是否存在布隆过滤器中
     * @param object
     * @return
     */
    public boolean isExistInBloom(Object object){
        return bloomFilter.isExist(object);
    }

    /**
     * 大范围查找匹配的key,(替代不好的keys命令)
     * @param hashKey
     * @param matchValue
     * @param count
     * @return
     * @throws IOException
     */
    public Set<String> scan(String hashKey, String matchValue, int count) throws IOException {
        return stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Set<String> keys = Sets.newHashSet();
                Jedis jedis = (Jedis) redisConnection.getNativeConnection();
                ScanParams scanParams = new ScanParams();
                scanParams.match("*"+matchValue+"*");
                scanParams.count(count);
                ScanResult<String> scanResult = jedis.scan("0",scanParams);
                while (scanResult.getResult()!=null){
                    keys.addAll(scanResult.getResult());
                    if (!scanResult.getStringCursor().equals("0")){
                        scanResult = jedis.scan(scanResult.getStringCursor(),scanParams);
                    }else {
                        break;
                    }
                }
                return keys;
            }
        });
    }

    /**
     * 管道集合查询命令，减少开销
     * @param keys
     * @param objectClass
     * @return
     */
    public List<Object> selectWithPipelined(List<String> keys, Class<T> objectClass){
        return stringRedisTemplate.executePipelined(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                redisConnection.openPipeline();
                keys.stream().forEach(key->{
                    stringRedisConnection.get(key);
                });
                return null;
            }
        });
    }

}
