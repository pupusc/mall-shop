package com.wanmi.sbc.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @Author lvzhenwei
 * @Description 秒杀商品抢购资格失效还库存MQ Topic
 * @Date 18:59 2019/6/17
 * @Param
 * @return
 **/
@Component
public interface FlashSaleOrderCancelReturnStockTopic {

    @Output(MQConstant.FLASH_SALE_ORDER_CANCEL_RETURN_STOCK_TOPIC_OUTPUT)
    MessageChannel output();

    @Input(MQConstant.FLASH_SALE_ORDER_CANCEL_RETURN_STOCK_TOPIC_INPUT)
    SubscribableChannel input();
}
