package com.wanmi.perseus.mq.sensors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.bean.EventRecord;
import com.wanmi.perseus.mq.sensors.bean.SensorsMessageDto;
import com.wanmi.perseus.mq.sensors.constant.SensorsMessageConstants;
import com.wanmi.perseus.mq.sensors.processor.SensorsMessageSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@EnableBinding(SensorsMessageSink.class)
public class SensorsMessageConsumer {

    @Autowired
    private ISensorsAnalytics sensorsAnalytics;

    @StreamListener(SensorsMessageConstants.Q_SENSORS_MESSAGE_CONSUMER)
    public void sensorsConsume(String message) {
        log.info("神策消费者收到消息:{}", message);
        try {
            List<SensorsMessageDto> sensorsMessageDtos = JSONArray.parseArray(message, SensorsMessageDto.class);
            for (SensorsMessageDto sensorsMessageDto : sensorsMessageDtos) {
                EventRecord eventRecord = EventRecord.builder().isLoginId(sensorsMessageDto.getLoginId()).setEventName(sensorsMessageDto.getEventName())
                        .setDistinctId(sensorsMessageDto.getDistinctId()).addProperties(sensorsMessageDto.getProperties()).build();
                sensorsAnalytics.track(eventRecord);
            }
        }catch (Exception e){
            log.error("神策埋点消息发送异常:" + message, e);
        }
    }
}
