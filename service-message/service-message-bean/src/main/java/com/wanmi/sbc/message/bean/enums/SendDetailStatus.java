package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-5
 * \* Time: 15:01
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiEnum
public enum SendDetailStatus {
    @ApiEnumProperty(" 0：失败")
    FAILED,

    @ApiEnumProperty("1：成功")
    SUCCESS;
    @JsonCreator
    public SendDetailStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
