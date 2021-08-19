package com.wanmi.sbc.message.bean.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author xuyunpeng
 * @Description 消息接收类型枚举类
 * @Date 14:28 2019/1/6
 * @Param
 * @return
 **/
@ApiEnum
public enum MessageSendType {

    @ApiEnumProperty(" 0：全部会员")
    ALL,

    @ApiEnumProperty("1：按会员等级")
    LEVEL,

    @ApiEnumProperty("2：按标签")
    TAG,

    @ApiEnumProperty("3：按人群")
    GROUP,

    @ApiEnumProperty("4：指定会员")
    CUSTOMER;
    @JsonCreator
    public static MessageSendType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
