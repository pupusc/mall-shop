package com.soybean.mall.order.mq;

import com.soybean.common.mq.TopicExchangeRabbitMqUtil;
import com.soybean.mall.order.gift.service.PayOrderGiftRecordPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: mq消息生产者
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/18 2:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
@Slf4j
public class MqOrderGiftRecordConsumer {

    @Autowired
    private PayOrderGiftRecordPointService payOrderGiftRecordPointService;

    /**
     * 接收下单
     */
    @RabbitListener(queues = {TopicExchangeRabbitMqUtil.QUEUE_CREATE_ORDER_GIFT_RECORD})
    @RabbitHandler
    public void createOrderGiftRecordMessage(String message) {
        log.info("MqOrderGiftRecordConsumer getOrderGiftRecordMessage {}", message);
        payOrderGiftRecordPointService.afterCreateOrder(message);
    }

    /**
     * 支付消息
     */
    @RabbitListener(queues = {TopicExchangeRabbitMqUtil.QUEUE_PAY_ORDER_GIFT_RECORD})
    @RabbitHandler
    public void payOrderGiftRecordMessage(String message) {
        log.info("MqOrderGiftRecordConsumer payOrderGiftRecordMessage {}", message);
        payOrderGiftRecordPointService.afterPayOrderLock(message);
    }
}
