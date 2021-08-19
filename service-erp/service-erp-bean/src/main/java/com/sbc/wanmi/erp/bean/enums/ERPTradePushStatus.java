package com.sbc.wanmi.erp.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sbc-background
 * @description: 订单推送状态枚举
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-30 13:55
 **/
@ApiEnum
public enum ERPTradePushStatus {


    @ApiEnumProperty("0: 未推送")
    NO_PUSH("NO_PUSH", "未推送"),

    @ApiEnumProperty("1:推送成功")
    PUSHED_SUCCESS("PUSHED_SUCCESS", "推送成功"),

    @ApiEnumProperty("2:推送失败")
    PUSHED_FAIL("PUSHED_FAIL", "推送失败");

    ERPTradePushStatus(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    private String stateId;

    private String description;

    private static Map<String, ERPTradePushStatus> tradePushERPStatusMap = new HashMap<>();

    static {
        Arrays.asList(ERPTradePushStatus.values())
                .forEach(
                        t -> tradePushERPStatusMap.put(t.getStateId(), t)
                );
    }

    @JsonCreator
    public static ERPTradePushStatus forValue(String stateId){
        return tradePushERPStatusMap.get(stateId);
    }

    @JsonValue
    public String toValue() {
        return this.getStateId();
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
