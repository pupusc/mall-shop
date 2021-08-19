package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 短信平台类型枚举类
 * @Date 10:23 2019/12/6
 * @Param
 * @return
 **/
@ApiEnum
public enum SmsSettingType {

    @ApiEnumProperty(" 0：阿里云短信平台")
    ALIYUN,
    @ApiEnumProperty(" 1：华信短信平台")
    HUAXIN;

    @JsonCreator
    public SmsSettingType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
