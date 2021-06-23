package com.wanmi.sbc.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName GoodsAboutNumSink
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/4/17 15:30
 **/
public interface GoodsAboutNumSink {

    /**
     * 商品收藏量mq 对应消费方法
     *
     * @return
     */
    @Input(MQConstant.GOODS_COLLECT_NUM)
    SubscribableChannel goodsCollectNumMqConsumer();

    /**
     * 商品收藏量mq 对应消费方法
     *
     * @return
     */
    @Input(MQConstant.GOODS_EVALUATE_NUM)
    SubscribableChannel goodsEvaluateNumMqConsumer();

    /**
     * 商品收藏量mq 对应消费方法
     *
     * @return
     */
    @Input(MQConstant.GOODS_SALES_NUM)
    SubscribableChannel goodsSalesNumMqConsumer();

    /**
     * 商品收藏量mq 对应消费方法
     *
     * @return
     */
    @Input(MQConstant.POINTS_GOODS_SALES_NUM)
    SubscribableChannel pointsGoodsSalesNumMqConsumer();
}
