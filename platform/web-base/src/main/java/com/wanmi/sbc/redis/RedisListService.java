package com.wanmi.sbc.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author djk
 */
@Service
public class RedisListService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListService.class);

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 从redis 中查询数据
     */
    public boolean putAll(final String key, List<T> list, long seconds) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        operations.rightPushAll(key, list);
        redisTemplate.expire(key, seconds, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 从redis 中查询数据
     */
    public List<T> findByRange(final String key, Integer start, Integer end) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        if (start > size) {
            return Collections.emptyList();
        }
        if (end > size) {
            end = size.intValue();
        }
        List<T> lists = operations.range(key, start, end);
        return lists;
    }

    /**
     * 从redis 中查询数据
     */
    public boolean delKey(final String key) {
        return redisTemplate.delete(key);
    }

}