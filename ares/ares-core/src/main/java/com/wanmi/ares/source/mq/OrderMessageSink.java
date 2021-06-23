package com.wanmi.ares.source.mq;

import com.wanmi.ares.constants.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: songhanlin
 * @Date: Created In 10:54 2019-04-04
 * @Description: 订单消息接收处理 Sink
 */
public interface OrderMessageSink {

    @Input(MQConstant.Q_ARES_ORDER_CREATE)
    SubscribableChannel createOrder();

    @Input(MQConstant.Q_ARES_ORDER_LIST_CREATE)
    SubscribableChannel createOrderList();

    @Input(MQConstant.Q_ARES_ORDER_PAY)
    SubscribableChannel payOrder();

    @Input(MQConstant.Q_ARES_ORDER_RETURN)
    SubscribableChannel returnOrder();

}
