package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 下架原因
 * 0未上架 1已上架 2部分上架
 * Created by zhangjin on 2017/3/22.
 */
@ApiEnum
public enum UnAddedFlagReason {

    @ApiEnumProperty("0: 平台删除")
    BOSSDELETE,
    @ApiEnumProperty("1: 供应商删除")
    PROVIDERDELETE,
    @ApiEnumProperty("3: 供应商下架")
    PROVIDERUNADDED;
    @JsonCreator
    public UnAddedFlagReason fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
