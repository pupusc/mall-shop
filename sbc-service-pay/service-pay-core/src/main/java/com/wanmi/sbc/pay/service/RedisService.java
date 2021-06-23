package com.wanmi.sbc.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>redis service</p>
 * Created by of628-wenzhi on 2018-08-20-下午5:18.
 */
@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return
     */
    public void delete(final String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Delete cache fail and key : " + key);
        }
    }

    /**
     * 保存数据到redis中
     */
    public boolean put(final String key, final Serializable value) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                connection.set(redisTemplate.getStringSerializer().serialize(key),
                        new JdkSerializationRedisSerializer().serialize(value));
                return true;
            });
        } catch (Exception e) {
            log.error("Put value to redis fail...", e);
        }

        return false;
    }

    /**
     * 保存字符串到redis中
     */
    public boolean setString(final String key, final String value) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
                redisConnection.set(redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(value));
                return true;
            });
        } catch (Exception e) {
            log.error("putString value to redis fail...", e);
        }

        return false;
    }

    public boolean hset(final String key, final String field, final String value) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
                    redisConnection.hSet(redisTemplate.getStringSerializer().serialize(key),
                            redisTemplate.getStringSerializer().serialize(field),
                            redisTemplate.getStringSerializer().serialize(value)));
        } catch (Exception e) {
            log.error("hset value to redis fail...", e);
        }

        return false;
    }


    public String hget(final String key, final String field) {
        try {
            return redisTemplate.execute((RedisCallback<String>) redisConnection -> {
                byte[] bytes = redisConnection.hGet(redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(field));
                return null != bytes ? redisTemplate.getStringSerializer().deserialize(bytes) : null;
            });
        } catch (Exception e) {
            log.error("hget value from redis fail...", e);
        }

        return null;
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return
     */
    public boolean hdelete(final String key, final String field) {
        try {
            redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
                Long res = redisConnection.hDel(redisTemplate.getStringSerializer().serialize(key), redisTemplate
                        .getStringSerializer().serialize(field));
                return res != null ? Boolean.TRUE : Boolean.FALSE;
            });
        } catch (Exception e) {
            log.error("hget value from redis fail...", e);
        }
        return Boolean.FALSE;
    }

    /**
     * 从redis 中查询数据
     */
    public Object get(final String key) {
        try {
            return redisTemplate.execute((RedisCallback<Object>) connection ->
                    new JdkSerializationRedisSerializer()
                            .deserialize(connection.get(redisTemplate.getStringSerializer().serialize(key))));
        } catch (Exception e) {
            log.error("Get value from  redis fail...", e);
        }
        return null;
    }

    /**
     * 从redis 中查询字符串对象
     */
    public String getString(final String key) throws SbcRuntimeException {
        try {
            return redisTemplate.execute((RedisCallback<String>) connection -> {
                byte[] bytes = connection.get(redisTemplate.getStringSerializer().serialize(key));
                return null != bytes ? redisTemplate.getStringSerializer().deserialize(bytes) : null;
            });
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }


    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 利用redis 分布式锁
     *
     * @param key
     * @return
     */
    public boolean setNx(final String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
                redisConnection.setNX(redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(key)));
    }

    /**
     * 以毫秒为单位设置key的超时时间
     *
     * @param key        key
     * @param expireTime 超时时间
     * @return boolean
     */
    public boolean expireByMilliseconds(String key, Long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 以秒为单位设置key的超时时间
     *
     * @param key        key
     * @param expireTime 超时时间
     * @return boolean
     */
    public boolean expireBySeconds(String key, Long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 以分钟为单位设置key的超时时间
     *
     * @param key        key
     * @param expireTime 超时时间
     * @return boolean
     */
    public boolean expireByMinutes(String key, Long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 对存储在指定key的数值执行原子的加1操作
     *
     * @param key key
     * @return
     */
    public Long incrKey(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.incr(redisTemplate.getStringSerializer().serialize(key))
        );
    }


    /**
     * 从redis 中查询数据
     */
    public <T> T getObj(final String key,Class<T> clazz) {
        String value = getString(key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return JSONObject.parseObject(value,clazz);
    }

    /**
     * 从redis 中查询数据
     */
    public <T> List<T> getList(final String key,Class<T> clazz) {
        String value = getString(key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return JSONObject.parseArray(value,clazz);
    }

    /**
     * 保存对象字符串到redis中
     *
     * @param key
     * @param obj
     * @param seconds 失效时间，-1的时候表示持久化
     * @return
     */
    public boolean setObj(final String key, final Object obj,final long seconds) {
        String value = JSONObject.toJSONString(obj);
        return this.setString(key,value,seconds);
    }

    /**
     * 保存字符串到redis中,失效时间单位秒
     */
    public boolean setString(final String key, final String value,final long seconds) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
                redisConnection.setEx(redisTemplate.getStringSerializer().serialize(key),
                        seconds,redisTemplate.getStringSerializer().serialize(value));
                return true;
            });
        } catch (Exception e) {
            log.error("putString value to redis fail...", e);
        }
        return false;
    }
}
