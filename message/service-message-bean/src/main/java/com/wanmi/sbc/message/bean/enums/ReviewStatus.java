package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 审核状态枚举类
 * @Date 10:28 2019/12/6
 * @Param
 * @return
 **/
@ApiEnum
public enum ReviewStatus {

    @ApiEnumProperty(" 0：待审核")
    PENDINGREVIEW,

    @ApiEnumProperty("1：审核通过")
    REVIEWPASS,

    @ApiEnumProperty("2：审核未通过")
    REVIEWFAILED,

    @ApiEnumProperty("3：待提交")
    WAITSUBMIT;

    @JsonCreator
    public static ReviewStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
