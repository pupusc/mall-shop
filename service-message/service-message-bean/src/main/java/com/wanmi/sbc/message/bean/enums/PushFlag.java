package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author xuyunpeng
 * @Description push推送标识枚举类
 * @Date 14:28 2019/1/6
 * @Param
 * @return
 **/
@ApiEnum
public enum PushFlag {

    @ApiEnumProperty(" 0：push消息")
    PUSH,

    @ApiEnumProperty("1：运营计划")
    OPERATION_PLAN;
    @JsonCreator
    public PushFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
