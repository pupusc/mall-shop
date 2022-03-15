package com.wanmi.sbc.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.goods.api.constant.GoodsJmsDestinationConstants;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyAlreadyGrouponNumRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyStatisticsNumRequest;
import com.wanmi.sbc.marketing.api.provider.constants.MarketingJmsDestinationConstants;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityModifyStatisticsNumByIdRequest;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.constant.MessageConstants;
import com.wanmi.sbc.order.api.request.esCustomerFunds.EsCustomerFundsSaveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeBackRestrictedRequest;
import com.wanmi.sbc.order.bean.dto.SensorsMessageDto;
import com.wanmi.sbc.order.bean.enums.BackRestrictedType;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 订单状态变更生产者
 * @Autho qiaokang
 * @Date：2019-03-05 17:47:18
 */
@Slf4j
@Service
@EnableBinding(value = {OrderSink.class})
public class OrderProducerService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 订单支付后，发送MQ消息
     * @param tradeVO
     */
    public void sendMQForOrderPayed(TradeVO tradeVO) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED).send(new GenericMessage<>(JSONObject.toJSONString(tradeVO)));

    }

    /**
     * 发送订单支付、订单完成MQ消息
     * @param tradeVO
     */
    public void sendMQForOrderPayedAndComplete(TradeVO tradeVO) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED_AND_COMPLETE).send(new GenericMessage<>(JSONObject.toJSONString(tradeVO)));

    }

    /**
     * 订单完成，发送MQ消息
     * @param tradeId
     */
    public void sendMQForOrderComplete(String tradeId) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_COMPLETE).send(new GenericMessage<>(tradeId));

    }

    /**
     * 积分订单完成，发送MQ消息
     * @param tradeId
     */
    public void sendMQForPointsOrderComplete(String tradeId) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_POINTS_ORDER_COMPLETE).send(new GenericMessage<>(tradeId));

    }

    /**
     * 分销订单退款作废后，发送MQ消息
     * @param tradeVO
     */
    public void sendMQForOrderRefundVoid(TradeVO tradeVO) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_REFUND_VOID).send(new GenericMessage<>(JSONObject.toJSONString(tradeVO)));

    }

    /**
     * 订单支付成功后调用第三方平台同步，发送MQ消息
     * @param businessId 业务id
     */
    public void sendMQForThirdPlatformSync(String businessId) {
        try {
            resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_THIRD_PLATFORM_SYNC).send(new GenericMessage<>(businessId));
        }catch (Exception e){
            log.error("订单支付成功后调用发起方MQ异常", e);
        }
    }

    /**
     * 超过一定时间未支付订单，自动取消订单
     * @param orderId
     * @param millis
     * @return
     */
    public Boolean cancelOrder(String orderId,Long millis){
        Boolean send = resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_CANCEL_ORDER_PRODUCER).send
                (MessageBuilder.withPayload(orderId).setHeader("x-delay", millis ).build());
        //超时未支付订单——超时未支付
        this.backRestrictedPurchaseNum(orderId,null,BackRestrictedType.ORDER_SETTING_TIMEOUT_CANCEL);
        return send;
    }

    /**
     * 团长开团-开启延迟消息
     * @param grouponNo
     * @param millis
     * @return
     */
    public Boolean sendOpenGroupon(String grouponNo,Long millis){
        Boolean send = resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_PRODUCER).send
                (MessageBuilder.withPayload(grouponNo).setHeader("x-delay", millis ).build());
        return send;
    }

    /**
     * 团长开团- C端消息推送
     * @param grouponNo
     * @param grouponActivityId
     * @return
     */
    public Boolean sendOpenGrouponMsgToC(String grouponNo,String grouponActivityId){
        GrouponInstanceVO grouponInstanceVO = new GrouponInstanceVO();
        grouponInstanceVO.setGrouponActivityId(grouponActivityId);
        grouponInstanceVO.setGrouponNo(grouponNo);
        Boolean send = resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_MESSAGE_PUSH_TO_C).send
                (new GenericMessage<>(JSONObject.toJSONString(grouponInstanceVO)));
        return send;
    }


    /**
     * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
     * @param grouponActivityId
     * @param grouponNum
     * @param grouponOrderStatus
     * @return
     */
    public Boolean updateStatisticsNumByGrouponActivityId(String grouponActivityId, Integer grouponNum, GrouponOrderStatus grouponOrderStatus){
        GrouponActivityModifyStatisticsNumByIdRequest request = new GrouponActivityModifyStatisticsNumByIdRequest(grouponActivityId,grouponNum,grouponOrderStatus);
        return resolver.resolveDestination(MarketingJmsDestinationConstants.Q_MARKET_GROUPON_MODIFY_STATISTICS_NUM).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 根据活动ID、SKU编号集合批量更新已成团人数
     * @param grouponActivityId
     * @param goodsInfoIds
     * @param alreadyGrouponNum
     * @return
     */
    public Boolean updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, List<String> goodsInfoIds, Integer alreadyGrouponNum){
        GrouponGoodsInfoModifyAlreadyGrouponNumRequest request = new GrouponGoodsInfoModifyAlreadyGrouponNumRequest(grouponActivityId,goodsInfoIds,alreadyGrouponNum);
        return resolver.resolveDestination(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ALREADY_GROUPON_NUM).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 根据活动ID、SKU编号更新商品销售量、订单量、交易额
     * @param grouponActivityId
     * @param goodsInfoId
     * @param goodsSalesNum
     * @param orderSalesNum
     * @param tradeAmount
     * @return
     */
    public Boolean updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, String goodsInfoId, Integer goodsSalesNum,
                                                                       Integer orderSalesNum, BigDecimal tradeAmount){
        GrouponGoodsInfoModifyStatisticsNumRequest request = new GrouponGoodsInfoModifyStatisticsNumRequest(grouponActivityId,goodsInfoId,goodsSalesNum,orderSalesNum,tradeAmount);
        return resolver.resolveDestination(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ORDER_PAY_STATISTICS).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 拼团订单-支付成功，订单异常，自动退款
     * @param trade
     */
    public void sendGrouponOrderAutoRefund(Trade trade) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_PAY_SUCCESS_AUTO_REFUND).send(new GenericMessage<>(JSONObject.toJSONString(trade)));
    }

    /**
     * 发送push、站内信、短信
     * @param request
     */
    public void sendMessage(MessageMQRequest request){
        resolver.resolveDestination(MessageConstants.Q_SMS_SERVICE_MESSAGE_SEND).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 拼团结束3小时前校验拼团人数
     * @param grouponNo
     * @param millis
     * @return
     */
    public boolean sendGrouponNumLimit(String grouponNo, Long millis){
        Boolean send = resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_JOIN_NUM_LIMIT_PRODUCER).send
                (MessageBuilder.withPayload(grouponNo).setHeader("x-delay", millis ).build());
        return send;
    }


    /**
     * 返回订单中限售商品
     * @param orderId
     * @param backRestrictedType
     * @return
     */
    public Boolean backRestrictedPurchaseNum(String orderId,String backOrderId, BackRestrictedType backRestrictedType){
        TradeBackRestrictedRequest restrictedRequest = TradeBackRestrictedRequest.builder()
                .tradeId(orderId)
                .backOrderId(backOrderId)
                .backRestrictedType(backRestrictedType).build();
        Boolean send = resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_REDUCE_RESTRICTED_PURCHASE_NUM)
                .send(new GenericMessage<>(JSONObject.toJSONString(restrictedRequest)));
        return send;
    }



    /**
     * 更新es会员资金信息
     * @param
     */
    public void sendEsCustomerFunds(EsCustomerFundsSaveRequest request) {
        try {
            resolver.resolveDestination(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS).send(new GenericMessage<>(request));
        }catch (Exception e){
            log.error("订单支付成功后调用同步esMQ异常", e);
        }
    }


    /**
     * 批量更新es会员资金信息
     * @param
     */
    public void sendEsCustomerFundsList(List<CustomerFundsForEsVO> lists) {
        try {
            resolver.resolveDestination(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS_LIST).send(new GenericMessage<>(lists));
        }catch (Exception e){
            log.error("订单支付成功后调用批量同步esMQ异常", e);
        }
    }

    /**
     * 发送神策埋点消息
     * @param sensorsMessageDto
     */
    public void sendSensorsMessage(List<SensorsMessageDto> sensorsMessageDto) {
        try {
            resolver.resolveDestination(JmsDestinationConstants.Q_SENSORS_MESSAGE_PRODUCER).send(new GenericMessage<>(sensorsMessageDto));
        }catch (Exception e){
            log.error("发送神策埋点失败", e);
        }
    }

//    /**
//     * 新增会员权益处理订单成长值临时表
//     * @param
//     */
//    public void orderGrowthValueTemp(OrderGrowthValueTemp orderGrowthValueTemp) {
//        try {
//            orderGrowthValueTempConsumptionSink.sendCoupon().send(new GenericMessage<>(JSONObject.toJSONString(orderGrowthValueTemp)));
//        }catch (Exception e){
//            log.error("新增会员权益处理订单成长值临时表MQ异常", e);
//        }
//    }


    /**
     * 订单发货后，发送MQ消息
     * @param trade
     */
    public void sendMQForOrderDelivered(Trade trade) {
        resolver.resolveDestination(JmsDestinationConstants.Q_OPEN_ORDER_DELIVERED_PRODUCER).send(new GenericMessage<>(trade));
    }
}
