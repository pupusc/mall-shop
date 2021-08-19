package com.wanmi.sbc.marketing.coupon.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding
public interface PaidCouponMqSink {
    String OUTPUT = "q-paid-rights-issue-coupons-output";
    String INPUT = "q-paid-rights-issue-coupons-input";

    @Input(INPUT)
    SubscribableChannel receiveCoupon();

    @Output(OUTPUT)
    MessageChannel sendCoupon();
}
