package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 短信类型美枚举类
 * @Date 10:33 2019/12/6
 * @Param
 * @return
 **/
@ApiEnum
public enum SmsType {

    @ApiEnumProperty(" 0：验证码")
    VERIFICATIONCODE,

    @ApiEnumProperty("1：短信通知")
    NOTICE,

    @ApiEnumProperty("2：推广短信")
    PROMOTE,

    @ApiEnumProperty("3：国际/港澳台消息")
    INTERNATIONAL;
    @JsonCreator
    public static SmsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
