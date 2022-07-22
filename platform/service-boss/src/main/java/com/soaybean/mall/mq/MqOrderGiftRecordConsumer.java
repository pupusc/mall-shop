package com.soaybean.mall.mq;

import com.soybean.common.mq.TopicExchangeRabbitMqUtil;
import com.soybean.mall.order.api.provider.order.PayOrderGiftRecordProvider;
import com.soybean.mall.order.api.request.record.OrderGiftRecordMqReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 2:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class MqOrderGiftRecordConsumer {

    @Autowired
    private PayOrderGiftRecordProvider payOrderGiftRecordProvider;

    /**
     * 接收下单
     */
    @RabbitListener(queues = {TopicExchangeRabbitMqUtil.QUEUE_CREATE_ORDER_GIFT_RECORD})
    public void createOrderGiftRecordMessage(String message) {
        log.info("MqOrderGiftRecordConsumer getOrderGiftRecordMessage {}", message);
//        payOrderGiftRecordPointService.afterCreateOrder(message);
        OrderGiftRecordMqReq orderGiftRecordMqReq = new OrderGiftRecordMqReq();
        orderGiftRecordMqReq.setMessage(message);
        payOrderGiftRecordProvider.afterCreateOrder(orderGiftRecordMqReq);
    }

    /**
     * 支付消息
     */
    @RabbitListener(queues = {TopicExchangeRabbitMqUtil.QUEUE_PAY_ORDER_GIFT_RECORD})
    public void payOrderGiftRecordMessage(String message) {
        log.info("MqOrderGiftRecordConsumer payOrderGiftRecordMessage {}", message);
        OrderGiftRecordMqReq orderGiftRecordMqReq = new OrderGiftRecordMqReq();
        orderGiftRecordMqReq.setMessage(message);
        payOrderGiftRecordProvider.afterPayOrderLock(orderGiftRecordMqReq);
    }
}
