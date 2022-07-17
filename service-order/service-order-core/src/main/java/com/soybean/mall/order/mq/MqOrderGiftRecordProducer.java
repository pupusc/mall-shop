package com.soybean.mall.order.mq;

import com.alibaba.fastjson.JSON;
import com.soybean.common.mq.TopicExchangeRabbitMqUtil;
import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
public class MqOrderGiftRecordProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送创建订单消息
     * @param recordMessageMq
     */
    public void sendCreateOrderGiftRecord(RecordMessageMq recordMessageMq) {
        String content = JSON.toJSONString(recordMessageMq);
        log.info("MqOrderGiftRecordProducer sendCreateOrderGiftRecord sendMq:{}", content);
        amqpTemplate.convertAndSend(TopicExchangeRabbitMqUtil.EXCHANGE_NAME_ORDER_GIFT_RECORD, "gift.record.create.order", content);
    }

    /**
     * 发送支付消息
     * @param recordMessageMq
     */
    public void sendPayOrderGiftRecord(RecordMessageMq recordMessageMq) {
        String content = JSON.toJSONString(recordMessageMq);
        log.info("MqOrderGiftRecordProducer sendPayOrderGiftRecord sendMq:{}", content);
        amqpTemplate.convertAndSend(TopicExchangeRabbitMqUtil.EXCHANGE_NAME_ORDER_GIFT_RECORD, "gift.record.pay.order", content);
    }
}
