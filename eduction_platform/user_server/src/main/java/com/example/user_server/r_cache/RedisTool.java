package com.example.user_server.r_cache;

import com.alibaba.fastjson.JSON;
import com.example.user_server.entity.User;
import com.example.user_server.tool.BloomFilter;
import org.apache.curator.shaded.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    /**
     * 管道集合添加命令，减少开销
     * @param map
     */
    public void addWithPipelined(Map<String,T> map){
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                redisConnection.openPipeline();
                map.keySet().stream().forEach(key->{
                    stringRedisConnection.set(key,JSON.toJSONString(map.get(key)));
                });
                return null;
            }
        });
    }
    /**
     * 管道集合删除命令，减少开销
     * @param keys
     */
    public void deleteWithPipelined(List<String> keys){
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                redisConnection.openPipeline();
                keys.stream().forEach(stringRedisConnection::del);
                return null;
            }
        });
    }

    /**
     * 事务处理增删改
     * @param methodMap
     * @throws IllegalAccessException
     */
    public void openTransaction(Map<User, Method> methodMap) throws IllegalAccessException{
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.multi();
        methodMap.keySet().stream().forEach(key->{
            try {
                if (methodMap.get(key).getName().contains("set")){
                    methodMap.get(key).invoke(stringRedisTemplate,key.getUser_id(),key);
                }else {
                    methodMap.get(key).invoke(stringRedisTemplate,key.getUser_id());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        stringRedisTemplate.exec();
    }

    /**
     * 设置位图
     * @param key
     * @param index
     * @param sign
     */
    public void setBit(String key, long index, boolean sign){
        stringRedisTemplate.opsForValue().setBit(key,index,sign);
    }

    /**
     * 取得位图对应位置的bit值
     * @param key
     * @param list
     * @return
     */
    public List<Object> getBit(String key, List<Integer> list){
        return stringRedisTemplate.executePipelined(new RedisCallback<List<Boolean>>() {
            @Override
            public List<Boolean> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.openPipeline();
                list.stream().forEach(index->{
                    stringRedisConnection.getBit(key,index);
                });
                return null;
            }
        });
    }
    /*public boolean getBit(String key, int... index){
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create();
        BitFieldSubCommands.BitFieldType bitFieldType = null;
        for (int i =0; i<index.length; i ++){
            bitFieldType = BitFieldSubCommands.BitFieldType.signed(index[i]);
            bitFieldSubCommands.get(bitFieldType);
        }
        stringRedisTemplate.opsForValue().bitField(key,bitFieldSubCommands);
        return stringRedisTemplate.opsForValue().getBit(key, index[0]);
    }*/

    /**
     * 添加HypeLogLog对象
     * @param key
     * @param values
     */
    public void addHypeLogLog(String key, List<String> values){
        HyperLogLogOperations<String,String> hyperLogLogOperations= stringRedisTemplate.opsForHyperLogLog();
        values.stream().forEach(value->{
            hyperLogLogOperations.add(key, (String []) values.toArray());
        });
    }

    /**
     * 获取当前HypeLogLog的总计
     * @param key
     * @return
     */
    public long getHypeLogLog(String key){
        HyperLogLogOperations<String,String> hyperLogLogOperations= stringRedisTemplate.opsForHyperLogLog();
        return hyperLogLogOperations.size(key);
    }
}
