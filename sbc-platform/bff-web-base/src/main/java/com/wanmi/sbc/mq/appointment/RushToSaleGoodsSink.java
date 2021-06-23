package com.wanmi.sbc.mq.appointment;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName RushToSaleGoodsSink
 * @Description 立刻抢购mq处理
 * @Author zxd
 * @Date 2020/05/25 9:58
 **/
@EnableBinding
public interface RushToSaleGoodsSink {

    String INPUT = MQConstant.RUSH_TO_SALE_GOODS_INPUT;

    String OUTPUT = MQConstant.RUSH_TO_SALE_GOODS_OUTPUT;

    @Input(INPUT)
    SubscribableChannel immediatelySaleGoods();

    @Output(OUTPUT)
    SubscribableChannel output();
}
