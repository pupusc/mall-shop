package com.wanmi.sbc.redis;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author djk
 */
@Service
public class RedisListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListService.class);


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从redis 中查询数据
     */
    @Transactional
    public void putAll(String key, List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        redisTemplate.setEnableTransactionSupport(true);
        //redisTemplate.setEnableTransactionSupport(true)在本方法中不能使用get去取值或者去watch

        //redisTemplate.multi();
        Boolean hasKey = redisTemplate.delete(key);

        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        ListOperations<String, JSONObject> operations = (ListOperations<String, JSONObject>) redisTemplate.opsForList();
        operations.rightPushAll(key, list);
        //redisTemplate.exec();
        //redisTemplate.setEnableTransactionSupport(false);
    }

    @Transactional
    public void putList(String key, List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        redisTemplate.setEnableTransactionSupport(true);
        //redisTemplate.setEnableTransactionSupport(true)在本方法中不能使用get去取值或者去watch

        Boolean hasKey = redisTemplate.delete(key);

        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        ListOperations<String, JSONObject> operations = (ListOperations<String, JSONObject>) redisTemplate.opsForList();
        operations.rightPushAll(key, list);
        redisTemplate.exec();
        //redisTemplate.setEnableTransactionSupport(false);
    }

    /**
     * 从redis 中查询数据
     */
    public boolean putAll(final String key, List list, long minutes) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        ListOperations<String, JSONObject> operations = redisTemplate.opsForList();
        operations.rightPushAll(key, list);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 从redis 中查询数据
     */
    public boolean putAllStr(final String key, List list, long minutes) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        ListOperations<String, String> operations = redisTemplate.opsForList();
        operations.rightPushAll(key, list);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 从redis 中查询数据
     */
    public List<JSONObject> findByRange(final String key, Integer start, Integer end) {
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        ListOperations<String, JSONObject> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        if (start > size) {
            return new ArrayList<>();
        }
        if (end > size) {
            end = size.intValue();
        }
        List<JSONObject> lists = operations.range(key, start, end);
        return lists;
    }

    public Integer getSize(final String key){
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        ListOperations<String, JSONObject> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        return Integer.parseInt(size.toString());
    }

    /**
     * 从redis 中查询数据
     */
    public List<JSONObject> findAll(final String key) {
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer(Object.class));
        ListOperations<String, JSONObject> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        List<JSONObject> lists = operations.range(key, 0, size);
        return lists;
    }

    /**
     * 从redis 中查询数据
     */
    public List<String> findByRangeString(final String key, Integer start, Integer end) {
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
        ListOperations<String, String> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        if (start > size) {
            return new ArrayList<>();
        }
        if (end > size) {
            end = size.intValue();
        }
        List<String> lists = operations.range(key, start, end);
        return lists;
    }

}