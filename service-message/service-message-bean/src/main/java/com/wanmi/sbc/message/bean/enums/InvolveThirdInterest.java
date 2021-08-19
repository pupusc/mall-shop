package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 短信签名是否涉及第三方利益
 * @Date 10:25 2019/12/6
 * @Param
 * @return
 **/
@ApiEnum
public enum InvolveThirdInterest {

    @ApiEnumProperty(" 0：否")
    NO,

    @ApiEnumProperty("1：是")
    YES;
    @JsonCreator
    public InvolveThirdInterest fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
