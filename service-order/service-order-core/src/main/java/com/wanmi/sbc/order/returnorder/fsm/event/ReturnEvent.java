package com.wanmi.sbc.order.returnorder.fsm.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 退货流程事件
 * Created by jinwei on 21/4/2017.
 */
public enum ReturnEvent {

    REMEDY("REMEDY", "修改退货单"),

    AUDIT("AUDIT", "审核退货单"),

    DELIVER("DELIVER", "发货"),

    RECEIVE("RECEIVE", "收到退货"),

    REFUND("REFUND", "退款"),

    REJECT_REFUND("REJECT_REFUND", "拒绝退款"),

    REJECT_RECEIVE("REJECT_RECEIVE", "拒绝收货"),

    COMPLETE("COMPLETE", "完成"),

    VOID("VOID", "作废"),

    REVERSE_REFUND("REVERSE_REFUND","扭转退款退单状态"),

    REVERSE_RETURN("REVERSE_RETURN","扭转退货退单状态"),

    REFUND_FAILED("REFUND_FAILED", "退款失败"),

    CLOSE_REFUND("CLOSE_REFUND","关闭退单");

    private String type;

    private String desc;

    ReturnEvent(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private static Map<String, ReturnEvent> map = new HashMap<>();

    static {
        Arrays.asList(ReturnEvent.values()).stream().forEach(
            t -> map.put(t.getType(), t)
        );
    }

    @JsonCreator
    public static ReturnEvent forValue(String type){
        return map.get(type);
    }

    @JsonValue
    public String toValue(){
        return this.getType();
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<String, ReturnEvent> getMap() {
        return map;
    }
}
