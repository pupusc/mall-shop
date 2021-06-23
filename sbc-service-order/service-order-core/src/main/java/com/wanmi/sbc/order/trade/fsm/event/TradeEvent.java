package com.wanmi.sbc.order.trade.fsm.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinwei on 28/3/2017.
 */
public enum TradeEvent {

    REMEDY("REMEDY", "订单修改"),

    AUDIT("AUDIT", "订单审核"),

    RE_AUDIT("RE_AUDIT","订单回审"),

    PAY("PAY", "订单支付"),

    DELIVER("DELIVER", "发货"),

    CONFIRM("CONFIRM", "确认订单"),

    COMPLETE("COMPLETE", "订单完成"),

    VOID("VOID", "作废"),

    REFUND("REFUND", "退款"),

    OBSOLETE_DELIVER("OBSOLETE_DELIVER", "作废发货记录"),

    OBSOLETE_PAY("OBSOLETE_PAY","作废支付记录"),

    REVERSE_REFUND("REVERSE_REFUND","扭转退款订单状态"),

    REVERSE_RETURN("REVERSE_RETURN","扭转退货订单状态"),

    JOIN_GROUPON("JOIN_GROUPON","拼团成功");


    private String type;

    private String description;

    TradeEvent(String type, String description) {
        this.type = type;
        this.description = description;
    }

    private static Map<String, TradeEvent> eventMap = new HashMap<>();

    static {
        eventMap.put("REMEDY", REMEDY);
        eventMap.put("PAY", PAY);
        eventMap.put("DELIVER", DELIVER);
        eventMap.put("CONFIRM", CONFIRM);
        eventMap.put("COMPLETE", COMPLETE);
        eventMap.put("OBSOLETE_DELIVER", OBSOLETE_DELIVER);
        eventMap.put("OBSOLETE_PAY", OBSOLETE_PAY);
        eventMap.put("VOID", VOID);
    }

    @JsonCreator
    public static TradeEvent forValue(String type) {
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
