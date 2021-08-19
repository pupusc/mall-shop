package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum SendFlag {
    @ApiEnumProperty(" 0: 不需要发送")
    NOT_SEND,

    @ApiEnumProperty("1：需要发送")
    NEED_SEND,

    @ApiEnumProperty("2：发送完成")
    SEND_END;


    @JsonCreator
    public SendFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
