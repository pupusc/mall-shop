package com.wanmi.sbc.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName ImmediatelyFlashSaleGoodsSink
 * @Description 立刻抢购商品异步处理抢购资格mq Sink
 * @Author lvzhenwei
 * @Date 2019/6/14 9:58
 **/
@EnableBinding
public interface RushToBuyFlashSaleGoodsSink {

    String INPUT = MQConstant.RUSH_TO_BUY_FLASH_SALE_GOODS_INPUT;

    String OUTPUT = MQConstant.RUSH_TO_BUY_FLASH_SALE_GOODS_OUTPUT;

    @Input(INPUT)
    SubscribableChannel immediatelyFlashSaleGoods();

    @Output(OUTPUT)
    SubscribableChannel output();
}
