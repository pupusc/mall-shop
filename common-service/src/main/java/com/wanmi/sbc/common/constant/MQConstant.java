package com.wanmi.sbc.common.constant;

public class MQConstant {
    public static final String PAID_CARD_MESSAGE_Q_NAME = "q.sms.service.paidcard.message.send";

    private MQConstant() {
    }

    /**
     * 记录操作日志MQ KEy
     */
    public static final String OPERATE_LOG_ADD = "q.operate.log.add";

    /**
     * 商家评价
     */
    public static final String STORE_EVALUATE_ADD = "q.store.evaluate.add";

    /**
     * 增加成长值MQ Key
     */
    public static final String INCREASE_GROWTH_VALUE = "q.increase.customer.growth.value";

    /**
     * 发放优惠券MQ Key
     */
    public static final String ISSUE_COUPONS = "q.level.rights.issue.coupons";

    /**
     * 统计商品收藏量MQ Key
     */
    public static final String GOODS_COLLECT_NUM = "q.goods.collect.num";

    /**
     * 统计商品销量MQ Key
     */
    public static final String GOODS_SALES_NUM = "q.goods.sales.num";

    /**
     * 统计商品评论数MQ Key
     */
    public static final String GOODS_EVALUATE_NUM = "q.goods.evaluate.num";

    /**
     * 统计积分商品销量MQ Key
     */
    public static final String POINTS_GOODS_SALES_NUM = "q.points_goods.sales.num";

    /**
     * 立刻抢购商品mq异步处理抢购资格key--input
     */
    public static final String RUSH_TO_BUY_FLASH_SALE_GOODS_INPUT = "q-rush-to-buy-flash-sale-goods-input";

    /**
     * 立刻抢购商品mq异步处理抢购资格key--output
     */
    public static final String RUSH_TO_BUY_FLASH_SALE_GOODS_OUTPUT = "q-rush-to-buy-flash-sale-goods-output";

    /**
     * 抢购商品订单未进行下单，将对应对应商品库存归还延迟队列MQ output key
     */
    public static final String FLASH_SALE_ORDER_CANCEL_RETURN_STOCK_TOPIC_OUTPUT = "flash-sale-order-cancel-return-stock-topic-output";

    /**
     * 抢购商品订单未进行下单，将对应对应商品库存归还延迟队列MQ input key
     */
    public static final String FLASH_SALE_ORDER_CANCEL_RETURN_STOCK_TOPIC_INPUT = "flash-sale-order-cancel-return-stock-topic-input";


    /**
     * 立刻预约商品mq异步处理同步预约数量--input
     */
    public static final String RUSH_TO_APPOINTMENT_SALE_GOODS_INPUT = "q-rush-to-appointment-sale-goods-input";

    /**
     * 立刻预约商品mq异步处理同步预约数量--output
     */
    public static final String RUSH_TO_APPOINTMENT_SALE_GOODS_OUTPUT = "q-rush-to-appointment-sale-goods-output";

    /**
     * 立刻抢购商品mq异步处理--input
     */
    public static final String RUSH_TO_SALE_GOODS_INPUT = "q-rush-to-sale-goods-input";

    /**
     * 立刻抢购商品mq异步处理--output
     */
    public static final String RUSH_TO_SALE_GOODS_OUTPUT = "q-rush-to-sale-goods-output";


    /**
     * app push短信发送
     */
    public static final String Q_SMS_SERVICE_PUSH_ADD = "q.sms.service.push.add";

    /**
     * 短信发送
     */
    public static final String Q_SMS_SEND_MESSAGE_ADD = "q.sms.send.message.add";

    /**
     * 验证码短信发送
     */
    public static final String Q_SMS_SEND_CODE_MESSAGE_ADD = "q.sms.send.message.code.add";

    /**
     * 消息发送
     */
    public static final String Q_SMS_SERVICE_MESSAGE_SEND = "q.sms.service.message.send";

    /**
     * elastic模块-标品库-初始化
     */
    public static final String Q_ES_STANDARD_INIT = "q.es.standard.init";

    /**
     * elastic模块-商品-初始化
     */
    public static final String Q_ES_GOODS_INIT = "q.es.goods.init";


    /**
     * 会员注册成功，发送订单支付MQ消息，同步ES
     */
   public static final String Q_ES_SERVICE_CUSTOMER_REGISTER = "q.es.service.customer.register";

    /**
     * 修改会员基本信息成功，发送订单支付MQ消息，同步ES
     */
    public static final String Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO = "q.es.service.customer.modify.base.info";

    /**
     * 修改会员账号成功，发送订单支付MQ消息，同步ES
     */
    public static final String Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT = "q.es.service.customer.modify.customer.account";

    /**
     * 删除会员账号成功，发送订单支付MQ消息，同步ES
     */
    public static final String Q_ES_SERVICE_CUSTOMER_DEL_CUSTOMER_INFO = "q.es.service.customer.del.customer.info";

    /**
     * 更新会员是否分销员字段，发送订单支付MQ消息，同步ES
     */
    public static final String Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_DISTRIBUTOR = "q.es.service.customer.modify.customer.isDistributor";

    /**
     * 新增积分兑换券，发送订单支付MQ消息，同步ES
     */
    public static final String Q_ES_SERVICE_COUPON_ADD_POINTS_COUPON = "q.es.service.coupon.add.points.coupon";
}
