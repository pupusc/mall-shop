package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-10-14 16:25
 */
@ApiEnum
public enum RFMType {

    @ApiEnumProperty(" 0：R")
    R,

    @ApiEnumProperty("1：F")
    F,

    @ApiEnumProperty("2：M")
    M;
    @JsonCreator
    public RFMType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
