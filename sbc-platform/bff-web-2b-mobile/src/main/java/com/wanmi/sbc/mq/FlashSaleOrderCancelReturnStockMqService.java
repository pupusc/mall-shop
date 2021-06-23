package com.wanmi.sbc.mq;

import com.wanmi.sbc.common.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

/**
 * @ClassName FlashSaleOrderCancleReturnStockMq
 * @Description 秒杀商品抢购资格失效还库存MQ
 * @Author lvzhenwei
 * @Date 2019/6/12 9:59
 **/
@Slf4j
@EnableBinding(FlashSaleOrderCancelReturnStockTopic.class)
public class FlashSaleOrderCancelReturnStockMqService {

    @Autowired
    private FlashSaleOrderCancelReturnStockTopic topic;

    /**
     * @Author lvzhenwei
     * @Description 秒杀商品抢购资格失效还库存MQ 生产者
     * @Date 10:03 2019/6/12
     * @Param [message]
     * @return void
     **/
    public void flashSaleOrderCancelReturnStock(String message) {
        topic.output().send(MessageBuilder.withPayload(message).setHeader("x-delay", Constants.FLASH_SALE_GOODS_QUALIFICATIONS_VALIDITY_PERIOD*60*1000).build());
    }
}
