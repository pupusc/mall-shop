package com.wanmi.sbc.mq.appointment;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName RushToAppointmentSaleGoodsSink
 * @Description 立刻预约同步数量
 * @Author zxd
 * @Date 2020/05/25 9:58
 **/
@EnableBinding
public interface RushToAppointmentSaleGoodsSink {

    String INPUT = MQConstant.RUSH_TO_APPOINTMENT_SALE_GOODS_INPUT;

    String OUTPUT = MQConstant.RUSH_TO_APPOINTMENT_SALE_GOODS_OUTPUT;

    @Input(INPUT)
    SubscribableChannel immediatelyAppointmentSaleGoods();

    @Output(OUTPUT)
    SubscribableChannel output();
}
