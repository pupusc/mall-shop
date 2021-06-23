package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.flashsale.service.FlashSaleService;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.request.flashsale.FlashSaleGoodsOrderCancelReturnStockRequest;
import com.wanmi.sbc.redis.RedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName FlashSaleGoodsOrderCancelReturnStockConsumerService
 * @Description 秒杀商品订单取消还库存
 * @Author lvzhenwei
 * @Date 2019/8/5 15:15
 **/
@Service
@EnableBinding(FlashSaleGoodsOrderCancelReturnStockSink.class)
public class FlashSaleGoodsOrderCancelReturnStockConsumerService {

    @Autowired

    private RedissonClient redissonClient;

    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private RedisService redisService;

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 秒杀商品订单取消还库存MQ消费方法
     * @Date 18:58 2019/6/17
     * @Param [message]
     **/
    @StreamListener(JmsDestinationConstants.Q_FLASH_SALE_GOODS_ORDER_CANCEL_RETURN_STOCK)
    public void flashSaleOrderCancelReturnStockReceive(String message) {
        FlashSaleGoodsOrderCancelReturnStockRequest request = JSONObject.parseObject(message, FlashSaleGoodsOrderCancelReturnStockRequest.class);
        //增加库存（加锁，保证高并发下库存是正确的）
        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.FLASH_SALE_GOODS_INFO_STOCK + request.getFlashSaleGoodsId());
        rLock.lock();
        try {
            String flashSaleGoodsInfoKey = RedisKeyConstant.FLASH_SALE_GOODS_INFO + request.getFlashSaleGoodsId();
            FlashSaleGoodsVO flashSaleGoodsVO = redisService.getObj(flashSaleGoodsInfoKey, FlashSaleGoodsVO.class);
            flashSaleGoodsVO.setStock(flashSaleGoodsVO.getStock() + request.getFlashSaleGoodsNum());
            redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
    }
}
