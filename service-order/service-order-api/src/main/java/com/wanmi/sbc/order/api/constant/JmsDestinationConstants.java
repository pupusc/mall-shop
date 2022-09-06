package com.wanmi.sbc.order.api.constant;

/**
 * @Description: MQ消息目的地常量
 * @Autho qiaokang
 * @Date：2019-03-05 17:54:47
 */
public interface JmsDestinationConstants {

    /**
     * 订单支付后，发送订单支付MQ消息
     */
    String Q_ORDER_SERVICE_ORDER_PAYED = "q.order.service.order.payed";

    /**
     * 订单完成时，发送订单完成MQ消息
     */
    String Q_ORDER_SERVICE_ORDER_COMPLETE = "q.order.service.order.complete";
    /**
     * 订单完成时，发送订单完成MQ消息
     */
    String Q_ORDER_SERVICE_POINTS_ORDER_COMPLETE = "q.order.service.points_order.complete";

    /**
     * 订单退款作废时，发送订单MQ消息
     */
    String Q_ORDER_SERVICE_ORDER_REFUND_VOID = "q.order.service.order.refund.void";

    /**
     * 退单状态变更，发送MQ消息
     */
    String Q_ORDER_SERVICE_RETURN_ORDER_FLOW = "q.order.service.return.order.flow";

    /**
     * 发送订单支付、订单完成MQ消息
     */
    String Q_ORDER_SERVICE_ORDER_PAYED_AND_COMPLETE = "q.order.service.order.payed.and.complete";


    /**
     * 订单提交，发送一个超过一定时间未支付，取消订单MQ生产者消息通道
     */
    String Q_ORDER_SERVICE_CANCEL_ORDER_PRODUCER = "cancel-order-producer";

    /**
     * 订单提交，发送一个超过一定时间未支付，取消订单MQ消费者消息通道
     */
    String Q_ORDER_SERVICE_CANCEL_ORDER_CONSUMER = "cancel-order-consumer";

    /**
     * 团长开团延迟MQ生产者消息通道
     */
    String Q_ORDER_SERVICE_OPEN_GROUPON_PRODUCER = "open-groupon-producer";

    /**
     * 团长开团延迟MQ消费者消息通道
     */
    String Q_ORDER_SERVICE_OPEN_GROUPON_CONSUMER = "open-groupon-consumer";

    /**
     * 开团发送消息推送（C端展示）
     */
    String Q_ORDER_SERVICE_OPEN_GROUPON_MESSAGE_PUSH_TO_C= "q.order.service.open.groupon.msg.push.to.c";

    /**
     * 秒杀商品订单取消订单发送mq还库存
     */
    String Q_FLASH_SALE_GOODS_ORDER_CANCEL_RETURN_STOCK = "flash_sale_goods_order_cancel_return_stock";

    /**
     * 拼团订单-支付成功，订单异常，自动退款
     */
    String Q_ORDER_SERVICE_GROUPON_PAY_SUCCESS_AUTO_REFUND = "q.order.service.groupon.pay.success.auto.refund";

    /**
     * 参团人数延迟消息生产者
     */
    String Q_ORDER_SERVICE_GROUPON_JOIN_NUM_LIMIT_PRODUCER = "groupon-num-limit-producer";

    /**
     * 参团人数延迟消息消费这
     */
    String Q_ORDER_SERVICE_GROUPON_JOIN_NUM_LIMIT_CONSUMER = "groupon-num-limit-consumer";

    /**
     * 业务员交接数据
     */
    String Q_ORDER_SERVICE_MODIFY_EMPLOYEE_DATA = "q.order.service.modify.employee.data";

    /**
     * 异常订单发送限售信息 ——（取消订单，超时未支付，退货，退款，审批未通过）
     */
    String Q_ORDER_SERVICE_REDUCE_RESTRICTED_PURCHASE_NUM =  "q.order.service.reduce.restricted.purchase.num";

    /**
     * 第三方平台订单-支付成功，订单下单同步
     */
    String Q_ORDER_SERVICE_THIRD_PLATFORM_SYNC =  "q.order.service.third.platform.sync";


    /**
     * 修改es会员信息
     */
    String Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS = "q.es.modify.or.add.customer.funds";


    /**
     * 批量修改es会员信息
     */
    String Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS_LIST = "q.es.modify.or.add.customer.funds.list";

    /**
     * 修改或保存积分订单抵扣异常信息
     */
    String Q_ORDER_MODIFY_OR_ADD_TRADE_POINTS_EXCEPTION = "q.order.modify.or.add.trade.points.exception";

    /**
     * 新增会员权益处理订单成长值 临时表
     */
    String  GROWTH_VALUE_MEMBER_EQUITY_ORDER = "growth.value.member.equity.order";

    String PROVIDER_TRADE_ORDER_PUSH = "provider.trade.order.push";

//    String PROVIDER_TRADE_DELIVERY_STATUS_SYNC = "provider.trade.delivery.status.sync";

    String PROVIDER_TRADE_ORDER_PUSH_CONFIRM="provider.trade.order.push.confirm";

    String PROVIDER_TRADE_ORDER_PUSH_CONFIRM_QUEUE="provider.trade.order.push.confirm.group-1";

    String PROVIDER_TRADE_DELIVERY_STATUS_SYNC_CONFIRM_QUEUE="provider.trade.delivery.status.sync.confirm.group-1";

    /**
     * 神策埋点消息
     */
    String Q_SENSORS_MESSAGE_PRODUCER = "sensorsMessageProducer";

    /**
     * 订单发货后，发送订单发货MQ消息
     */
    String Q_OPEN_ORDER_DELIVERED_PRODUCER = "q-open-order-delivered-producer";

    String Q_OPEN_ORDER_DELIVERED_CONSUMER = "q-open-order-delivered-consumer";
}
