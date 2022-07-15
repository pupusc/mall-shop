package com.wanmi.sbc.order.purchase;

import com.wanmi.sbc.order.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liang Jun
 * @desc 购物车勾选缓存
 * @date 2022-07-15 16:54:00
 */
@Service
public class PurchaseTickService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;
    /**
     * 添加勾选
     */
    public boolean doTick(Long cartId, String skuId) {
//        redisService.setObj(RedisKeyConstant.TRADE_ITME_SNAPSHOT+tradeItemSnapshot.getTerminalToken(), tradeItemSnapshot, 24*60*60);
        return false;
    }
    /**
     * 取消勾线
     */
    public boolean unTick(Long cartId, String skuId) {
        return false;
    }
    /**
     * 获取购物车勾选的sku
     */
    public List<String> getTicks(Long cartId) {
        return null;
    }

}
