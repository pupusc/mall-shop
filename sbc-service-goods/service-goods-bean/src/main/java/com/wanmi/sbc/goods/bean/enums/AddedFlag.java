package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 上下架类型
 * 0未上架 1已上架 2部分上架 3 定时上架
 * Created by zhangjin on 2017/3/22.
 */
@ApiEnum
public enum AddedFlag {

    @ApiEnumProperty("0: 未上架")
    NO,
    @ApiEnumProperty("1: 已上架")
    YES,
    @ApiEnumProperty("2: 部分上架")
    PART;
    @JsonCreator
    public static AddedFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
