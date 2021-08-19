package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author xuyunpeng
 * @Description 消息类型枚举类
 * @Date 14:28 2019/1/6
 * @Param
 * @return
 **/
@ApiEnum
public enum ReadFlag {

    @ApiEnumProperty(" 0：未读")
    NO,

    @ApiEnumProperty("1：已读")
    YES;

    @JsonCreator
    public ReadFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
