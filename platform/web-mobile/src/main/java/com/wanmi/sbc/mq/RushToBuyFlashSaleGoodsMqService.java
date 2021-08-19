package com.wanmi.sbc.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * @ClassName ImmediatelyFlashSaleGoodsMqService
 * @Description 立刻抢购商品异步处理抢购资格mq
 * @Author lvzhenwei
 * @Date 2019/6/14 10:09
 **/
@Slf4j
@Component
@EnableBinding
public class RushToBuyFlashSaleGoodsMqService {

    @Autowired
    private RushToBuyFlashSaleGoodsSink rushToBuyFlashSaleGoodsSink;

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 立刻抢购商品异步处理抢购资格mq
     * @Date 10:15 2019/6/14
     * @Param [immediatelyFlashSaleGoodsMessage]
     **/
    public void rushToBuyFlashSaleGoodsMq(String rushToBuyFlashSaleGoodsMessage) {
        rushToBuyFlashSaleGoodsSink.output().send(new GenericMessage<>(rushToBuyFlashSaleGoodsMessage));
    }
}
