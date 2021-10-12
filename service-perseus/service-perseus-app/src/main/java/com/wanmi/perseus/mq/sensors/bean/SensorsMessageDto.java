package com.wanmi.perseus.mq.sensors.bean;

import lombok.Data;

import java.util.Map;

@Data
public class SensorsMessageDto {

    private Boolean loginId;
    private String distinctId;
    private String eventName;
    private Map<String, Object> properties;
}
