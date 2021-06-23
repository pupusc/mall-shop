package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.marketing.coupon.model.entity.TradeCouponSnapshot;
import com.wanmi.sbc.marketing.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-04-08 20:50
 */
@Service
public class TradeCouponSnapshotService {


    @Autowired
    private RedisService redisService;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param returnOrder
     */
    public void addTradeCouponSnapshot(TradeCouponSnapshot returnOrder) {
        redisService.setObj(RedisKeyConstant.TRADE_COUPON_SNAPSHOT+returnOrder.getBuyerId(),
                returnOrder,24*60*60);//设置24小时过期
    }

    /**
     * 删除文档
     *
     * @param buyerId
     */
    public void deleteTradeCouponSnapshot(String buyerId) {
        redisService.delete(RedisKeyConstant.TRADE_COUPON_SNAPSHOT+buyerId);
    }

    /**
     * 获取文档
     * @param buyerId
     * @return
     */
    public TradeCouponSnapshot getByBuyerId(String buyerId){
        return redisService.getObj(RedisKeyConstant.TRADE_COUPON_SNAPSHOT+buyerId,TradeCouponSnapshot.class);
    }

}
