package com.wanmi.sbc.account.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/16 10:29
 * @version: 1.0
 */
@Service
public class RedisService {

	@Resource
	private RedisTemplate<String,Object> redisTemplate;

	/**
	 * 功能描述：返回 key 所关联的字符串值。
	 * @param key
	 * @return
	 */
	public Object get(String key){
		return  redisTemplate.opsForValue().get(key);
	}

	/**
	 * 增加数值
	 * @param key 键
	 * @param delta 浮点数
	 * @return
	 */
	public Double incrByFloat(String key, double delta){
		return  redisTemplate.opsForValue().increment(key,delta);
	}

	/**
	 * 减少数值
	 * @param key
	 * @param delta
	 * @return
	 */
	public Double decrByFloat(String key, double delta){
		return  redisTemplate.opsForValue().increment(key,-delta);
	}

	/**
	 * 批量删除key
	 * @param list
	 */
	public void del(List list){
		 redisTemplate.delete(list);
	}

}
