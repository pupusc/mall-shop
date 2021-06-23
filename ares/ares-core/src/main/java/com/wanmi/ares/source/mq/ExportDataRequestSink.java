package com.wanmi.ares.source.mq;

import com.wanmi.ares.constants.MQConstant;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: songhanlin
 * @Date: Created In 10:43 2019-04-04
 * @Description: 导出任务要求的消息队列 Sink
 */
@EnableBinding
public interface ExportDataRequestSink {

    String OUTPUT = "areas-export-output";
    String INPUT = "areas-export-input";

   /* @Input(MQConstant.Q_ARES_EXPORT_DATA_REQUEST)
    SubscribableChannel produceExportData();*/

    @Input(INPUT)
    SubscribableChannel produceExportData();

    @Output(OUTPUT)
    MessageChannel output();

}
