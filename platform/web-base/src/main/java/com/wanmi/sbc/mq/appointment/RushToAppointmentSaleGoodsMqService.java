package com.wanmi.sbc.mq.appointment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * @ClassName RushToAppointmentSaleGoodsMqService
 * @Description 立刻预约同步数量mq
 * @Author zxd
 * @Date 2020/05/25 10:30
 **/
@Slf4j
@Component
@EnableBinding
public class RushToAppointmentSaleGoodsMqService {

    @Autowired
    private RushToAppointmentSaleGoodsSink rushToAppointmentSaleGoodsSink;

    @Autowired
    private RushToSaleGoodsSink rushToSaleGoodsSink;

    /**
     * @return void
     * @Author zxd
     * @Description 立刻预约同步数量mq
     * @Date 10:15 2020/05/25
     * @Param [rushToAppointmentSaleGoodsMq]
     **/
    public void rushToAppointmentSaleGoodsMq(String rushToAppointmentSaleGoodsMessage) {
        log.info("-----发送立即预约MQ-----");
        rushToAppointmentSaleGoodsSink.output().send(new GenericMessage<>(rushToAppointmentSaleGoodsMessage));
    }

    /**
     * 立即抢购mq处理
     *
     * @param message
     */
    public void rushToSaleGoodsMq(String message) {
        rushToSaleGoodsSink.output().send(new GenericMessage<>(message));
    }
}
