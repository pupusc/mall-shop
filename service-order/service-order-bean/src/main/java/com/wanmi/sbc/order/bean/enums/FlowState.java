package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@ApiEnum(dataType = "java.lang.String")
public enum FlowState {

    @ApiEnumProperty("0: INIT 创建订单")
    INIT("INIT", "创建订单"),

    @ApiEnumProperty("1: REMEDY 修改订单")
    REMEDY("REMEDY", "修改订单"),

    @ApiEnumProperty("2: REFUND 已退款")
    REFUND("REFUND", "已退款"),

    @ApiEnumProperty("3: AUDIT 已审核")
    AUDIT("AUDIT", "已审核"),

    @ApiEnumProperty("4: DELIVERED_PART 部分发货")
    DELIVERED_PART("DELIVERED_PART", "部分发货"),

    @ApiEnumProperty("5: DELIVERED 已发货")
    DELIVERED("DELIVERED", "已发货"),

    @ApiEnumProperty("6: CONFIRMED 已确认")
    CONFIRMED("CONFIRMED", "已确认"),

    @ApiEnumProperty("7: COMPLETED 已完成")
    COMPLETED("COMPLETED", "已完成"),

    @ApiEnumProperty("8: VOID 已作废")
    VOID("VOID", "已作废"),

    @ApiEnumProperty("9: GROUPON 已参团")
    GROUPON("GROUPON","已参团"),

    @ApiEnumProperty("10: WAIT_PAY_EARNEST 待支付定金")
    WAIT_PAY_EARNEST("WAIT_PAY_EARNEST", "待支付定金"),

    @ApiEnumProperty("11: WAIT_PAY_TAIL 待支付尾款")
    WAIT_PAY_TAIL("WAIT_PAY_TAIL", "待支付尾款");

    private static Map<String, FlowState> flowStateMap = new HashMap<>();

    static {
        Arrays.asList(FlowState.values()).stream().forEach(
                t -> flowStateMap.put(t.getStateId(), t)
        );
    }


    private String stateId;

    private String description;

    FlowState(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    @JsonCreator
    public static FlowState forValue(String stateId) {
        return flowStateMap.get(stateId);
    }

    @JsonValue
    public String toValue() {
        return this.getStateId();
    }

    public String getStateId() {
        return stateId;
    }

    public String getDescription() {
        return description;
    }

}
