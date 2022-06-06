package com.soybean.elastic.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/6 2:10 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 存入hash 非原子性
     * @param key
     */
    public void putHashStr(String key,String hashKey, String hashValue) {
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
        redisTemplate.expire(key, 4, TimeUnit.HOURS);
    }

    /**
     * 获取hash内容
     * @param key
     */
    public String getHashStr(String key, String hashKey) {
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
        if (redisTemplate.opsForHash().hasKey(key, hashKey)) {
            return null;
        }
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }
}
