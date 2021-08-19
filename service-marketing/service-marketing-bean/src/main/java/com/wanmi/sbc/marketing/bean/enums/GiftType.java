package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 赠品赠送的方式
 * 0：全赠， 1：赠一个
 *
 * @author hht
 * @date 2018/1/29
 */
@ApiEnum
public enum GiftType {

    @ApiEnumProperty("0：全赠")
    ALL,

    @ApiEnumProperty("1：赠一个")
    ONE;

    @JsonCreator
    public GiftType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
