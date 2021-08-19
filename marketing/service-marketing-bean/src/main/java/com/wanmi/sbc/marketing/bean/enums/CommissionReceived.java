package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author : baijz
 * @Date : 2019/2/26 15 18
 * @Description :
 */
@ApiEnum(dataType = "java.lang.String")
public enum CommissionReceived {

    /**
     * 分销未入账
     */
    @ApiEnumProperty("0:未入账")
    UNRECEIVE("佣金未入账"),

    /**
     * 分销已入账
     */
    @ApiEnumProperty("1:分销已入账")
    RECEIVED("佣金已入账");

    private String type;

    public String getType() {
        return type;
    }

    CommissionReceived(String type) {
        this.type = type;
    }

    @JsonCreator
    public static CommissionReceived fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
