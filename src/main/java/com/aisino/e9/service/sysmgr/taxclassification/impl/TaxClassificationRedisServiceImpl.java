package com.aisino.e9.service.sysmgr.taxclassification.impl;

import com.aisino.e9.entity.taxclassification.pojo.TaxClassification;
import com.aisino.e9.service.sysmgr.taxclassification.TaxClassificationRedisService;
import com.aisino.e9.service.sysmgr.taxclassification.TaxClassificationService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-6-13.
 */
@Service("taxClassificationRedisService")
public class TaxClassificationRedisServiceImpl implements TaxClassificationRedisService {

    @Resource
    private TaxClassificationService taxClassificationService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean cacheKeyword() {
        final List<TaxClassification> taxClassifications = taxClassificationService.findAll();
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
                for (TaxClassification taxClassification : taxClassifications) {
                    valueOper.set(taxClassification.getCode(),taxClassification.getName());
                }
                return true;
            }
        }, false, true);
        return result;
    }


    @Override
    public String getNameByCode(final String code) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)throws DataAccessException {
                ValueOperations<String, String> operations = redisTemplate.opsForValue();
                return operations.get(code);
            }
        });
        return result;
    }
}
