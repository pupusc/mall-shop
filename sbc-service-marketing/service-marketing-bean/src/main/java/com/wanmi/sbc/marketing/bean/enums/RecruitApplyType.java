package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:53 2019/2/27
 * @Description: 申请条件
 */
@ApiEnum
public enum RecruitApplyType {

    @ApiEnumProperty("0：购买商品")
    BUY,

    @ApiEnumProperty("1：邀请注册")
    REGISTER;

    @JsonCreator
    public RecruitApplyType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
