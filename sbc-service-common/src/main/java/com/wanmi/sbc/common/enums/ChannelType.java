package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:49 2019/3/1
 * @Description: 分销渠道类型
 */
@ApiEnum
public enum ChannelType {

    @ApiEnumProperty("PC商城")
    PC_MALL,

    @ApiEnumProperty("商城")
    MALL,

    @ApiEnumProperty("小店")
    SHOP;

    @JsonCreator
    public static ChannelType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
