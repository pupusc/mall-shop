package com.soybean.mall.configuration.mq;

import com.soybean.common.mq.TopicExchangeRabbitMqUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/18 2:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Configuration
public class RabbitmqConfiguration {


    /**
     * exchangeOrderGiftRecord
     * @return
     */
    @Bean(name = "exchangeOrderGiftRecord")
    public TopicExchange exchangeOrderGiftRecord() {
        return new TopicExchange(TopicExchangeRabbitMqUtil.EXCHANGE_NAME_ORDER_GIFT_RECORD);
    }

    /**
     * queueOrderGiftRecord
     * @return
     */
    @Bean(name = "queueCreateOrderGiftRecord")
    public Queue queueCreateOrderGiftRecord() {
        return new Queue(TopicExchangeRabbitMqUtil.QUEUE_CREATE_ORDER_GIFT_RECORD);
    }

    @Bean
    public Binding bindingCreateOrderGiftRecord(@Qualifier("exchangeOrderGiftRecord") TopicExchange topicExchange, @Qualifier("queueCreateOrderGiftRecord") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(TopicExchangeRabbitMqUtil.TOPIC_CREATE_ORDER_GIFT_RECORD_ROUTER);
    }


    /**
     * queuePayOrderGiftRecord
     * @return
     */
    @Bean(name = "queuePayOrderGiftRecord")
    public Queue queuePayOrderGiftRecord() {
        return new Queue(TopicExchangeRabbitMqUtil.QUEUE_PAY_ORDER_GIFT_RECORD);
    }

    @Bean
    public Binding bindingOrderGiftRecord(@Qualifier("exchangeOrderGiftRecord") TopicExchange topicExchange, @Qualifier("queuePayOrderGiftRecord") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(TopicExchangeRabbitMqUtil.TOPIC_PAY_ORDER_GIFT_RECORD_ROUTER);
    }
}
