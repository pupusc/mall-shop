package com.soybean.common.mq;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/18 2:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class TopicExchangeRabbitMqUtil {

    /**
     * exchange
     */
    public static final String EXCHANGE_NAME_ORDER_GIFT_RECORD = "EXCHANGE_NAME_ORDER_GIFT_RECORD";

    /**
     * queue
     */
    public static final String QUEUE_CREATE_ORDER_GIFT_RECORD = "QUEUE_ORDER_GIFT_RECORD";

    /**
     * topic_router
     */
    public static final String TOPIC_CREATE_ORDER_GIFT_RECORD_ROUTER = "#.create.order";

    /**
     * queue
     */
    public static final String QUEUE_PAY_ORDER_GIFT_RECORD = "QUEUE_ORDER_GIFT_RECORD";

    /**
     * topic_router
     */
    public static final String TOPIC_PAY_ORDER_GIFT_RECORD_ROUTER = "#.pay.order";


    /**
     * queue
     */
    public static final String QUEUE_CANCEL_ORDER_GIFT_RECORD = "QUEUE_CANCEL_ORDER_GIFT_RECORD";

    /**
     * topic_router
     */
    public static final String TOPIC_CANCEL_ORDER_GIFT_RECORD_ROUTER = "#.cancel.order";

}
