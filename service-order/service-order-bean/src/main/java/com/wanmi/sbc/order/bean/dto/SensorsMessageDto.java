package com.wanmi.sbc.order.bean.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SensorsMessageDto {

    private Boolean loginId;
    private String distinctId;
    private String eventName;
    private Map<String, Object> properties;

    public void addProperty(String key, Object value){
        if(properties == null) properties = new HashMap<>();
        properties.put(key, value);
    }
}
