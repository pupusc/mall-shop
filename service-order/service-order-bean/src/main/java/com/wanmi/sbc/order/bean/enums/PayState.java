package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付状态
 * Created by jinwei on 15/3/2017.
 */
@ApiEnum
public enum PayState {

    @ApiEnumProperty("0: NOT_PAID 未支付")
    NOT_PAID("NOT_PAID", "未支付"),

    @ApiEnumProperty("1: UNCONFIRMED 待确认")
    UNCONFIRMED("UNCONFIRMED", "待确认"),

    @ApiEnumProperty("2: PAID 已支付")
    PAID("PAID", "已支付"),

    @ApiEnumProperty("3: PAID_EARNEST 已支付定金")
    PAID_EARNEST("PAID_EARNEST", "已支付定金");

    PayState(String stateId, String description){
        this.stateId = stateId;
        this.description = description;
    }

    private String stateId;

    private String description;

    private static Map<String, PayState> payStateMap = new HashMap<>();

    static {
        Arrays.asList(PayState.values()).stream()
            .forEach(
                t -> payStateMap.put(t.getStateId(), t)
            );
    }

    @JsonCreator
    public static PayState forValue(String stateId){
        return payStateMap.get(stateId);
    }

    @JsonValue
    public String toValue(){
        return this.getStateId();
    }

    public String getStateId() {
        return stateId;
    }

    public String getDescription() {
        return description;
    }
}
