package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 数据类型
 *  0:兑换码 1:券码+密钥 2:链接
 *
 */
@ApiEnum
public enum ProvideType {
    @ApiEnumProperty("0:兑换码")
    REDEMPTION_CODE,
    @ApiEnumProperty("1:券码+密钥")
    CODE_KEY,

    @ApiEnumProperty("2:链接")
    LINK;
    @JsonCreator
    public static ProvideType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
