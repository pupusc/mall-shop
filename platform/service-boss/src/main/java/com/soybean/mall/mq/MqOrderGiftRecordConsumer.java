package com.soybean.mall.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.soybean.common.mq.TopicExchangeRabbitMqUtil;
import com.soybean.mall.order.api.provider.order.PayOrderGiftRecordProvider;
import com.soybean.mall.order.api.request.record.OrderGiftRecordMqReq;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

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
    public void orderGiftRecordMessage(Message message, Channel channel) {
        log.info("MqOrderGiftRecordConsumer orderGiftRecordMessage {}", new String(message.getBody(), Charset.defaultCharset()));
//        payOrderGiftRecordPointService.afterCreateOrder(message);
        OrderGiftRecordMqReq orderGiftRecordMqReq = new OrderGiftRecordMqReq();
        orderGiftRecordMqReq.setMessage(new String(message.getBody(), Charset.defaultCharset()));
        try {
            BaseResponse baseResponse = payOrderGiftRecordProvider.afterRecordMessageOrder(orderGiftRecordMqReq);
            log.info("MqOrderGiftRecordConsumer orderGiftRecordMessage response:{}", JSON.toJSONString(baseResponse));
            if (Objects.equals(baseResponse.getCode(), CommonErrorCode.SUCCESSFUL)) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                try {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                } catch (IOException e) {
                    log.error("MqOrderGiftRecordConsumer orderGiftRecordMessage basicNack error", e);
                }
            }
        } catch (Exception ex) {
            log.error("MqOrderGiftRecordConsumer orderGiftRecordMessage error", ex);
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                log.error("MqOrderGiftRecordConsumer orderGiftRecordMessage basicNack error", e);
            }
        }
    }


    /**
     * 取消订单监听
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {TopicExchangeRabbitMqUtil.QUEUE_CANCEL_ORDER_GIFT_RECORD})
    public void orderCancelGiftRecordMessage(Message message, Channel channel) {
        log.info("MqOrderGiftRecordConsumer orderCancelGiftRecordMessage {}", new String(message.getBody(), Charset.defaultCharset()));
        OrderGiftRecordMqReq orderGiftRecordMqReq = new OrderGiftRecordMqReq();
        orderGiftRecordMqReq.setMessage(new String(message.getBody(), Charset.defaultCharset()));
        payOrderGiftRecordProvider.afterRecordMessageOrder(orderGiftRecordMqReq);
    }
}
