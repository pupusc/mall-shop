package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 首页类型: 0 商城首页 1 店铺首页
 * @author zhangwenchang
 */
@ApiEnum
public enum IndexType {

    @ApiEnumProperty("商城首页")
    INDEX,
    @ApiEnumProperty("店铺首页")
    STOREINDEX;

    @JsonCreator
    public static IndexType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
