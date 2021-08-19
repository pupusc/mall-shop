package com.wanmi.sbc.order.pointstrade.fsm.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinwei on 28/3/2017.
 */
public enum PointsTradeEvent {

    PAY("PAY", "订单支付"),

    DELIVER("DELIVER", "发货"),

    CONFIRM("CONFIRM", "确认订单"),

    COMPLETE("COMPLETE", "订单完成");


    private String type;

    private String description;

    PointsTradeEvent(String type, String description) {
        this.type = type;
        this.description = description;
    }

    private static Map<String, PointsTradeEvent> eventMap = new HashMap<>();

    static {
        eventMap.put("PAY", PAY);
        eventMap.put("DELIVER", DELIVER);
        eventMap.put("CONFIRM", CONFIRM);
        eventMap.put("COMPLETE", COMPLETE);
    }

    @JsonCreator
    public static PointsTradeEvent forValue(String type) {
        return eventMap.get(type);
    }

    @JsonValue
    public String toValue() {
        return this.getType();
    }


    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
