package com.wanmi.sbc.setting.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: songhanlin
 * @Date: Created In 14:55 2019-04-02
 * @Description: 日志Sink
 */
public interface LogSink {

    //@Input(MQConstant.OPERATE_LOG_ADD)
    SubscribableChannel receiveOperationLogMq();

}
