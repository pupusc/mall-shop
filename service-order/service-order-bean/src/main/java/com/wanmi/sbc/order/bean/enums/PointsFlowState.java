package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lvzhenwei
 * @Description 积分订单流程状态
 * @Date 16:03 2019/5/23
 * @Param
 * @return
 **/
@ApiEnum(dataType = "java.lang.String")
public enum PointsFlowState {

    @ApiEnumProperty("0: INIT 创建订单")
    INIT("INIT", "创建订单"),

    @ApiEnumProperty("3: AUDIT 已审核")
    AUDIT("AUDIT", "已审核"),

    @ApiEnumProperty("4: DELIVERED_PART 部分发货")
    DELIVERED_PART("DELIVERED_PART", "部分发货"),

    @ApiEnumProperty("5: DELIVERED 已发货")
    DELIVERED("DELIVERED", "已发货"),

    @ApiEnumProperty("6: CONFIRMED 已确认")
    CONFIRMED("CONFIRMED", "已确认"),

    @ApiEnumProperty("7: COMPLETED 已完成")
    COMPLETED("COMPLETED", "已完成");

    private static Map<String, PointsFlowState> flowStateMap = new HashMap<>();

    static {
        Arrays.asList(PointsFlowState.values()).stream().forEach(
                t -> flowStateMap.put(t.getStateId(), t)
        );
    }


    private String stateId;

    private String description;

    PointsFlowState(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    @JsonCreator
    public static PointsFlowState forValue(String stateId) {
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
