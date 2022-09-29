//package com.wanmi.sbc.order.mq;
//
//import com.alibaba.fastjson.JSONObject;
//import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
//import com.wanmi.sbc.erp.api.request.PushTradeRequest;
//import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
//import org.springframework.messaging.support.GenericMessage;
//import org.springframework.stereotype.Service;
//
//
///**
// * @Description: 非原erp发货单
// * @Autho liuxia
// */
//@Slf4j
//@Service
//@EnableBinding(value = {OrderSink.class})
//public class ProviderTradeOrderService {
//
//    @Autowired
//    private BinderAwareChannelResolver resolver;
//
//
////    /**
////     * 发货单发送MQ消息
////     *
////     * @param request
////     */
////    public void sendMQForProviderTrade(PushTradeRequest request) {
////        resolver.resolveDestination(JmsDestinationConstants.PROVIDER_TRADE_ORDER_PUSH).send(new GenericMessage<>(JSONObject.toJSONString(request)));
////
////    }
//
////    /**
////     * 状态查询MQ消息
////     * @param request
////     */
////    public void sendMQForDeliveryStatus(DeliveryQueryRequest request) {
////        resolver.resolveDestination(JmsDestinationConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC).send(new GenericMessage<>(JSONObject.toJSONString(request)));
////
////    }
//}
//
//
//
//
