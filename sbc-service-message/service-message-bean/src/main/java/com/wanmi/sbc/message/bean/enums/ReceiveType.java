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
public enum  ReceiveType {
    @ApiEnumProperty(" 0：全部会员")
    ALL,

    @ApiEnumProperty("1：会员等级")
    LEVEL,

    @ApiEnumProperty("2：会员人群")
    GROUP,

    @ApiEnumProperty("3：自定义选择")
    CUSTOM;
    @JsonCreator
    public ReceiveType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getContent(){
        switch (this){
            case ALL:
                return "全部会员";
            case LEVEL:
                return "%s";
            case GROUP:
                return "%s";
            case CUSTOM:
                return "自定义选择";
        }
        return "";
    }
}
