package com.wanmi.sbc.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding
public interface GoodsProviderSink {
    String OUTPUT = "q-goods-provider-status-modify-output";
    String INPUT = "q-goods-provider-status-modify-input";

    @Input(INPUT)
    SubscribableChannel providerStatus();

    @Output(OUTPUT)
    MessageChannel output();
}
