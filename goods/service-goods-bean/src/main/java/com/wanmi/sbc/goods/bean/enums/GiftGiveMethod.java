package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 赠送方式：0：默认全送 1:可选一种
 */
@ApiEnum
public enum GiftGiveMethod {

    @ApiEnumProperty("0: 默认全送")
    FREE,

    @ApiEnumProperty("1: 可选一种")
    CHOICE;

    @JsonCreator
    public static GiftGiveMethod fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
