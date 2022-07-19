package com.wanmi.sbc.order.purchase;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 购物车勾选缓存
 * @date 2022-07-15 16:54:00
 */
@Service
public class PurchaseCacheService {
    //勾选标记
    private static final String cartTickKey = "shopcart:tick:";
    //过期时间
    private static final long cartTickTime = 3 * 24 * 60 * 60;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 添加勾选
     */
    public void doTick(String cartId, String skuId) {
        redisTemplate.opsForSet().add(getCacheKey(cartId), skuId);
        restCacheTime(cartId);
    }

    /**
     * 添加勾选
     */
    public void doTicks(String cartId, List<String> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return;
        }
        redisTemplate.opsForSet().add(getCacheKey(cartId), skuIds.toArray(new String[]{}));
        restCacheTime(cartId);
    }

    /**
     * 取消勾线
     */
    public void unTick(String cartId, String skuId) {
        redisTemplate.opsForSet().remove(getCacheKey(cartId), skuId);
        restCacheTime(cartId);
    }

    /**
     * 取消勾选
     */
    public void unTicks(String userId, List<String> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return;
        }
        redisTemplate.opsForSet().remove(getCacheKey(userId), skuIds.toArray(new String[]{}));
        restCacheTime(userId);
    }

    /**
     * 重置商品勾选
     */
    public void resetTicks(String userId, List<String> skuIds) {
        redisTemplate.delete(getCacheKey(userId));
        doTicks(userId, skuIds);
    }

    public void clearTicks(String userId) {
        redisTemplate.delete(getCacheKey(userId));
    }

    /**
     * 获取购物车勾选的sku
     */
    public List<String> getTicks(String cartId) {
        if (StringUtils.isBlank(cartId)) {
            return Collections.emptyList();
        }
        return redisTemplate.opsForSet().members(getCacheKey(cartId)).stream().collect(Collectors.toList());
    }

    private String getCacheKey(String cartId) {
        if (cartId == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "购物车id不能为空");
        }
        return cartTickKey + cartId;
    }
    private void restCacheTime(String cartId) {
        redisTemplate.expire(getCacheKey(cartId), cartTickTime  , TimeUnit.SECONDS);
    }
}
