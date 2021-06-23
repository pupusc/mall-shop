package com.wanmi.sbc.mq;

import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author lvzhenwei
 * @Description 秒杀抢购商品订单取消还redis商品库存
 * @Date 19:23 2019/7/31
 * @Param
 * @return
 **/
public interface FlashSaleGoodsOrderCancelReturnStockSink {

    /**
     * @Author lvzhenwei
     * @Description 秒杀抢购商品订单取消还redis商品库存mq key
     * @Date 19:24 2019/7/31
     * @Param []
     * @return org.springframework.messaging.SubscribableChannel
     **/
    @Input(JmsDestinationConstants.Q_FLASH_SALE_GOODS_ORDER_CANCEL_RETURN_STOCK)
    SubscribableChannel flashSaleGoodsOrderCancelReturnStock();
}
