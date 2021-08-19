package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 5:51 PM 2018/8/8
 * @Description: 第三方登录方式
 */
@ApiEnum
public enum ThirdLoginType {
    @ApiEnumProperty("第三方登录方式：微信")
    WECHAT;

    @JsonCreator
    public ThirdLoginType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}