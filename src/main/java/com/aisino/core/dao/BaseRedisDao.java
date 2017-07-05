package com.aisino.core.dao;

import com.aisino.core.entity.BaseEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 为 on 2017-6-13.
 */
public class BaseRedisDao<K,V extends BaseEntity> {

    @Resource
    protected RedisTemplate<K, V> redisTemplate;

    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取 RedisSerializer
     */
    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }


    public boolean add(final V entity) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
                valueOper.set((K)entity.getId(), entity);
                return true;
            }
        }, false, true);
        return result;
    }

    public boolean batchAdd(final List<V> entitys) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
                for (V entity : entitys) {
                    valueOper.set((K)entity.getId(), entity);
                }
                return true;
            }
        }, false, true);
        return result;
    }

    public void delete(K key) {
        List<K> list = new ArrayList<>();
        list.add(key);
        delete(list);
    }

    public void delete(List<K> keys) {
        redisTemplate.delete(keys);
    }

    public boolean update(final V entity) {
        K id = (K)entity.getId();
        if (get(id) == null) {
            throw new NullPointerException("数据行不存在, key = " + id);
        }
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
                valueOper.set((K)entity.getId(), entity);
                return true;
            }
        });
        return result;
    }


    public V get(final K keyId) {
        V result = redisTemplate.execute(new RedisCallback<V>() {
            public V doInRedis(RedisConnection connection)
                    throws DataAccessException {
                ValueOperations<K, V> operations = redisTemplate.opsForValue();
                V entity = operations.get(keyId);
                return entity;
            }
        });
        return result;
    }

    public void rPushList(K key, V value) {
        redisTemplate.opsForList().rightPush(key,value);

    }

    public V lPopList(K key) {
        return redisTemplate.opsForList().leftPop(key);
    }


}
