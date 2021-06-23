package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: caofang
 * @Description: 处理状态,0：待处理，1：处理失败，2：处理成功
 */
@ApiEnum
public enum HandleStatus {

    @ApiEnumProperty("待处理")
    PENDING,

    @ApiEnumProperty("处理失败")
    PROCESSING_FAILED,

    @ApiEnumProperty("处理成功")
    SUCCESSFULLY_PROCESSED;

    @JsonCreator
    public static HandleStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}